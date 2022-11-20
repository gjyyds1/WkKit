package cn.wekyjay.www.wkkit.edit.prompt;

import org.bukkit.Bukkit;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.bukkit.entity.Player;

import cn.wekyjay.www.wkkit.WkKit;

public class GroupPrompt{
	
	public static void newConversation(Player player,String groupname) {
		Conversation conversation = new ConversationFactory(WkKit.getWkKit()) // 构建一个会话
	            .withFirstPrompt(new GroupPrompt_1()) // 设置在所有生成的对话中使用的第一个提示。
	            .withTimeout(60)
	            .buildConversation(player);
		conversation.getContext().setSessionData("groupname", groupname);
		conversation.begin();
	}

}

class GroupPrompt_1 extends ValidatingPrompt{
	// 询问要做什么
	@Override
	public String getPromptText(ConversationContext context) {
		return "你是否要删除" + context.getSessionData("groupname") + "?(Y/N)";
	}

	// 玩家输入的安全检测
	@Override
	protected boolean isInputValid(ConversationContext context, String input) {
		if(input.equalsIgnoreCase("Y") || input.equalsIgnoreCase("N")) return true; 
		return false; 
	}

	// InputValid返回有效时执行
	@Override
	protected Prompt acceptValidatedInput(ConversationContext context, String input) {
		if(input.equalsIgnoreCase("N")) return Prompt.END_OF_CONVERSATION;
		return new GroupPrompt_2(); // 下一个对话
	}
}
class GroupPrompt_2 extends ValidatingPrompt {

	
	@Override
	public String getPromptText(ConversationContext context) {
		return "是否也删除礼包组内的礼包?(Y/N)";

	}

	@Override
	protected boolean isInputValid(ConversationContext context, String input) {
		if(input.equalsIgnoreCase("Y") || input.equalsIgnoreCase("N")) return true; 
		return false; 
	}

	@Override
	protected Prompt acceptValidatedInput(ConversationContext context, String input) {
		if(input.equalsIgnoreCase("N")) Bukkit.dispatchCommand((Player)context.getForWhom(), "wk group remove " + context.getSessionData("groupname") + " true");
		else Bukkit.dispatchCommand((Player)context.getForWhom(), "wk group delete " + context.getSessionData("groupname"));
		return Prompt.END_OF_CONVERSATION;
	}
	
}
