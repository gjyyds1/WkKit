package cn.wekyjay.www.wkkit.command;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cn.wekyjay.www.wkkit.WkKit;
import cn.wekyjay.www.wkkit.config.LangConfigLoader;
import cn.wekyjay.www.wkkit.kit.Kit;
import cn.wekyjay.www.wkkit.kitcode.CodeManager;
import cn.wekyjay.www.wkkit.tool.WKTool;

public class KitCDK {
	public void onCommand(CommandSender sender, String[] args) {
		if(args.length < 2) {
			sender.sendMessage(LangConfigLoader.getStringWithPrefix("Commands.cdk_create", ChatColor.GREEN));
			sender.sendMessage(LangConfigLoader.getStringWithPrefix("Commands.cdk_verify", ChatColor.GREEN));
			sender.sendMessage(LangConfigLoader.getStringWithPrefix("Commands.cdk_exchange", ChatColor.GREEN));
			sender.sendMessage(LangConfigLoader.getStringWithPrefix("Commands.cdk_export", ChatColor.GREEN));
			return;
		}
		// CDK生成
		if(args[1].equalsIgnoreCase("create") && sender.isOp()) {
			if(args.length < 5) {
				sender.sendMessage(LangConfigLoader.getStringWithPrefix("Commands.cdk_create", ChatColor.GREEN));
				return;
			}
			int num = Integer.parseInt(args[2]); // 生成的兑换码个数
			List<String> kitlist = Arrays.asList(args[3].split(",")); // 兑换码包含的礼包
			List<String> cdklist = CodeManager.create((byte)1, num, 12, CodeManager.getPassword()); // 本次生成的兑换码
			String mark = args[4];
			// 遍历礼包是否存在
			for(String kit : kitlist) {
				if(Kit.getKit(kit) == null) kitlist.remove(kit);
			}
			// 遍历并保存CDK
			for(String cdk : cdklist) {
				// Kit
				StringBuilder sb = new StringBuilder();
				for(int i = 0; i < kitlist.size();i++) {
					if(i == kitlist.size() -1) sb.append(kitlist.get(i));
					else sb.append(kitlist.get(i) + ",");
				}
				WkKit.getCdkData().addCDKToFile(cdk, sb.toString(),  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), mark);
			}
			sender.sendMessage("§aCDK已生成！本次生成CDK：" + num + " 个");

		}
		// CDK验证
		if(args[1].equalsIgnoreCase("verify") && sender.hasPermission("wkkit.cdk.verify")) {
			if(args.length < 3) {
				sender.sendMessage(LangConfigLoader.getStringWithPrefix("Commands.cdk_verify", ChatColor.GREEN));
				return;
			}
			String CDK = args[2];
			if(CodeManager.VerifyCode(CDK) && WkKit.getCdkData().Contain_CDK(CDK)) {
				String status = "§aCDK可用";
				if(!WkKit.getCdkData().getCDKStatus(CDK).equals("Available")) status = "§c已被玩家 §e" +WkKit.getCdkData().getCDKStatus(CDK) + " §c使用";
				sender.sendMessage("§a========== §6§lCDK兑换 §a=========");
				sender.sendMessage("§8CDK: §9" + CDK);
				sender.sendMessage("§8CDK备注: §7" +  WkKit.getCdkData().getCDKMark(CDK));
				sender.sendMessage("§8生成日期: §a" + WkKit.getCdkData().getCDKDate(CDK));
				sender.sendMessage("§8使用情况: " + status);
				sender.sendMessage("§a==============================");
			}else {
				sender.sendMessage(LangConfigLoader.getStringWithPrefix("CDK_INVALID", ChatColor.RED));
			}
			return;
		}
		// CDK兑换
		if(args[1].equalsIgnoreCase("exchange")&& sender instanceof Player && sender.hasPermission("wkkit.cdk.exchange")) {
			if(args.length < 3) {
				sender.sendMessage(LangConfigLoader.getStringWithPrefix("Commands.cdk_exchange", ChatColor.GREEN));
				return;
			}
			String CDK = args[2];
			Player player = (Player)sender;
			if(CodeManager.VerifyCode(CDK) && WkKit.getCdkData().Contain_CDK(CDK) && WkKit.getCdkData().getCDKStatus(CDK).equals("Available")) {
				// 获得兑换码内含的礼包
				List<String> kitlist = Arrays.asList(WkKit.getCdkData().getCDKKits(CDK).split(","));
				// 遍历礼包是否存在
				for(String kit : kitlist) {
					if(Kit.getKit(kit) == null) kitlist.remove(kit);
				}
				// 判断玩家背包是否有剩余的空间
				if(WKTool.hasSpace(player, kitlist.size())) {
					for(String kit : kitlist) {
						player.getInventory().addItem(Kit.getKit(kit).getKitItem());
					}
					player.sendMessage("§aCDK兑换成功！");
				}else {//否则发送到礼包邮箱
					for(String kitname : kitlist) {
						if(WkKit.getPlayerData().contain_Mail(player.getName(),kitname)) {
							int num = WkKit.getPlayerData().getMailKitNum(player.getName(), kitname);
							WkKit.getPlayerData().setMailNum(player.getName(), kitname, num + 1);
						}else {
							WkKit.getPlayerData().setMailNum(player.getName(), kitname, 1);
						}

					}
					player.sendMessage("§aCDK兑换成功！已发送至礼包邮箱...");
				}
				WkKit.getCdkData().setCDKStatus(CDK, player.getName());
				return;
			}
			sender.sendMessage(LangConfigLoader.getStringWithPrefix("CDK_CANTUSE", ChatColor.YELLOW));
			return;
		}
		// 导出指定Mark的CDK
		if(args[1].equalsIgnoreCase("export")&& sender.isOp()) {
			if(args.length < 3) {
				sender.sendMessage(LangConfigLoader.getStringWithPrefix("Commands.cdk_export", ChatColor.GREEN));
				return;
			}
			String mark = args[2]; //mark
			File file = new File(WkKit.getWkKit().getDataFolder(),"Export");
			List<String> cdklist = new ArrayList<>(); //CDKlist
			// 如果mark ！= null,判断mark是否存在
			if(mark != null && WkKit.getCdkData().Contain_CDKMark(mark)) {
				cdklist = WkKit.getCdkData().findCDK(mark);
			}else {
				sender.sendMessage(LangConfigLoader.getStringWithPrefix("CDK_MARK_NONEXIST", ChatColor.RED));
				return;
			}
			if(!file.exists()) file.mkdir();
			try {
				RandomAccessFile ra;
				String foldername = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date()) + "-" + mark;
				ra = new RandomAccessFile(file.getAbsolutePath() + File.separatorChar + foldername + ".txt ","rw");
				ra.seek(ra.length());//代表从文件的结尾写不会覆盖，也就是文件的追加
				for(String cdk : cdklist) {
					ra.write(cdk.getBytes());
					ra.write("\n".getBytes());
				}
				ra.close();
				sender.sendMessage("§a已导出至 Export\\"+foldername+".txt");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}


		}
		
	}
}
