package me.blayyke.reflex.utils;

import net.dv8tion.jda.bot.sharding.ShardManager;
import net.dv8tion.jda.core.entities.*;

import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ParseUtils {
    public static Pattern USER_MENTION_PATTERN = Pattern.compile("^<@!?(\\d{17,21})>$");
    public static Pattern FULL_USER_REF_PATTERN = Pattern.compile("^(\\S.{0,30}\\S)\\s*#(\\d{4})$");
    public static Pattern LONG_PATTERN = Pattern.compile("^-?(\\d{1,21})$");
    public static Pattern CHANNEL_MENTION_PATTERN = Pattern.compile("^<#(\\d{17,21})>$");
    public static Pattern ROLE_MENTION_PATTERN = Pattern.compile("^<@&(\\d{17,21})>$");

    // <: 2-32 DIGITS : 17-21 DIGITS>
    public static Pattern EMOTE_MENTION_PATTERN = Pattern.compile("^<a?:(.{2,32}):(\\d{17,21})>$");

    public static List<TextChannel> getTextChannels(Guild guild, String query) {
        if (query == null) return Collections.emptyList();

        Matcher idMatcher = LONG_PATTERN.matcher(query);
        if (idMatcher.matches() && guild.getTextChannelById(idMatcher.group(1)) != null)
            return Collections.singletonList(guild.getTextChannelById(query));

        Matcher mentionMatcher = CHANNEL_MENTION_PATTERN.matcher(query);
        if (mentionMatcher.matches())
            return Collections.singletonList(guild.getTextChannelById(mentionMatcher.group(1)));

        return guild.getTextChannelsByName(query, true);
    }

    public static List<Emote> getEmotes(ShardManager shardManager, String query) {
        if (query == null) return Collections.emptyList();

        System.out.println(query);

        Matcher idMatcher = LONG_PATTERN.matcher(query);
        if (idMatcher.matches() && shardManager.getEmoteById(idMatcher.group(1)) != null)
            return Collections.singletonList(shardManager.getEmoteById(query));

        Matcher mentionMatcher = EMOTE_MENTION_PATTERN.matcher(query);

        if (mentionMatcher.matches()) {
            return Collections.singletonList(shardManager.getEmoteById(mentionMatcher.group(2)));
        }

        return shardManager.getEmotesByName(query, true);
    }

    public static List<VoiceChannel> getVoiceChannels(Guild guild, String query) {
        if (query == null) return Collections.emptyList();

        Matcher idMatcher = LONG_PATTERN.matcher(query);
        if (idMatcher.matches() && guild.getVoiceChannelById(idMatcher.group(1)) != null)
            return Collections.singletonList(guild.getVoiceChannelById(query));

        Matcher mentionMatcher = CHANNEL_MENTION_PATTERN.matcher(query);
        if (mentionMatcher.matches())
            return Collections.singletonList(guild.getVoiceChannelById(mentionMatcher.group(1)));

        return guild.getVoiceChannelsByName(query, true);
    }

    public static List<Role> getRoles(Guild guild, String query) {
        if (query == null) return Collections.emptyList();

        Matcher idMatcher = LONG_PATTERN.matcher(query);
        if (idMatcher.matches() && guild.getRoleById(idMatcher.group(1)) != null)
            return Collections.singletonList(guild.getRoleById(query));

        Matcher mentionMatcher = ROLE_MENTION_PATTERN.matcher(query);
        if (mentionMatcher.matches())
            return Collections.singletonList(guild.getRoleById(mentionMatcher.group(1)));

        return guild.getRolesByName(query, true);
    }

    public static List<Member> findMembers(Guild guild, String query) {
        if (query == null) return Collections.emptyList();

        Matcher mentionMatch = USER_MENTION_PATTERN.matcher(query);
        Matcher fullRefMatch = FULL_USER_REF_PATTERN.matcher(query);

        // Id search (123456789123456789)
        if (MiscUtils.isId(query) && guild.getMemberById(query) != null) {
            return Collections.singletonList(guild.getMemberById(query));
        }
        // Mention search (@John)
        if (mentionMatch.matches()) {
            Member member = guild.getMemberById(mentionMatch.group(1));
            if (member != null)
                return Collections.singletonList(member);
        }

        // Full ref search (John#0001)
        if (fullRefMatch.matches()) {
            String name = fullRefMatch.group(1).toLowerCase();
            String discriminator = fullRefMatch.group(2);

            List<Member> users = guild.getMembers()
                    .stream().filter(user -> user.getUser().getName().toLowerCase().equals(name)
                            && user.getUser().getDiscriminator().equals(discriminator))
                    .collect(Collectors.toList());
            if (!users.isEmpty())
                return users;
        }

        // None of the above were found, fall back to name search.
        return guild.getMembersByName(query, true);
    }
}