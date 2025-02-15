package de.Ste3et_C0st.FurnitureLib.Command;

import org.bukkit.command.CommandSender;

import de.Ste3et_C0st.FurnitureLib.main.FurnitureLib;
import de.Ste3et_C0st.FurnitureLib.main.ObjectID;

public class purgeCommand extends iCommand{

	public purgeCommand(String subCommand, String ...args) {
		super(subCommand);
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		//furniture purge <days>
		if(!hasCommandPermission(sender)) return;
		int purgeTime = FurnitureLib.getInstance().getPurgeTime();
		if(args.length==2){
			if(FurnitureLib.getInstance().isInt(args[1])){
				purgeTime = Integer.parseInt(args[1]);
			}else{
				sender.sendMessage(FurnitureLib.getInstance().getLangManager().getString("message.WrongArgument"));
				return;
			}
		}else{
			sender.sendMessage(FurnitureLib.getInstance().getLangManager().getString("message.WrongArgument"));
			return;
		}
		
		int i = 0;
		for(ObjectID id : FurnitureLib.getInstance().getFurnitureManager().getObjectList()){
			if(id.getUUID()!=null){
				boolean b = FurnitureLib.getInstance().checkPurge(id, id.getUUID(), purgeTime);
				if(b) i++;
			}
		}
		
		if(!FurnitureLib.getInstance().isPurgeRemove()){
			sender.sendMessage(FurnitureLib.getInstance().getLangManager().getString("message.PurgeMarked").replace("#AMOUNT#", i + ""));
			return;
		}
		
		sender.sendMessage(FurnitureLib.getInstance().getLangManager().getString("message.RemoveDistance").replace("#AMOUNT#", i + ""));
		return;
	}
}
