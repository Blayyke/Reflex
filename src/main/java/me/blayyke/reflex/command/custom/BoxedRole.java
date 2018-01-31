package me.blayyke.reflex.command.custom;

import net.dv8tion.jda.core.entities.Role;

public class BoxedRole {
    private Role role;

    BoxedRole(Role role) {
        this.role = role;
    }

    public String getName() {
        return role.getName();
    }

    public String getId() {
        return role.getId();
    }

    public String mention() {
        return role.getAsMention();
    }
}