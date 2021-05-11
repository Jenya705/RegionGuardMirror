package com.github.jenya705.command;

public class RGCommand extends AbstractCommandsContainer {

    public RGCommand(){

        getCommandExecutors().put("count", new RGCountCommand());
        getCommandExecutors().put("create", new RGCreateCommand());
        getCommandExecutors().put("confirm", new RGConfirmCommand());
        getCommandExecutors().put("list", new RGListCommand());
        getCommandExecutors().put("remove", new RGRemoveCommand());
        RGPosCommand rgPosCommand = new RGPosCommand();
        getCommandExecutors().put("pos1", rgPosCommand);
        getCommandExecutors().put("pos2", rgPosCommand);

    }

}
