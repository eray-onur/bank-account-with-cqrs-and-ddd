package com.techbank.account.cmd.application.commands;

import com.techbank.cqrs.core.commands.BaseCommand;

import java.util.UUID;

public class CloseAccountCommand extends BaseCommand {
    public CloseAccountCommand(String id) {
        super(id);
    }
}
