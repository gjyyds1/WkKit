package cn.wekyjay.www.wkkit.command;

import cn.wekyjay.www.wkkit.kit.Kit;
import cn.wekyjay.www.wkkit.tool.WKTool;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum TabSend {
	
	
    FIRST(Arrays.asList("send"),0,null,new int[]{1}),
	KIT_NAME(new ArrayList<>(),1,"send",new int[]{2}),
	PLAYER_NAME(WKTool.getPlayerNames(),1,"send",new int[]{3}),
	KIT_NUM(Arrays.asList("1"),1,"send",new int[]{4});
	
    private List<String> list;
    private int befPos;
    private String bef;
    private int[] num;
    
    
    
    private TabSend(List<String> list,int befPos, String bef, int[] num){
        this.list = list;
        this.befPos = befPos;
        this.bef = bef;
        this.num = num.clone();
    }
    
    public String getBef() {
		return bef;
	}
    public int getBefPos() {
		return befPos;
	}
    public List<String> getList() {
    	if(num[0] == 2)return list = Kit.getKitNames();
    	return list;
	}
    public int[] getNum() {
		return num;
	}
    
    
    public static List<String> returnList(String[] Para, int curNum, CommandSender sender) {
        for(TabSend tab : TabSend.values() ){
            if(tab.getBefPos()-1 >= Para.length){
                continue;
            }
            if((tab.getBef() == null || tab.getBef().equalsIgnoreCase(Para[tab.getBefPos()-1])) && Arrays.binarySearch(tab.getNum(),curNum)>=0){
            	List<String> list = new ArrayList<String>();
            	if(tab.getNum()[0] == 3) {
            		list.add("@All");
            		list.add("@Online");
            		list.add("@Me");
            	}
            	if(Para[tab.getNum()[0] - 1] != null) {
                	int length = Para[tab.getNum()[0] - 1].length();
                	String abc = Para[tab.getNum()[0] - 1];
                	for(String s : tab.getList()) {
                		if(s.regionMatches(true, 0, abc, 0, length)) list.add(s);
                	}
                	return list;
                }else {
                	tab.getList().addAll(list);
                	return tab.getList();
                }
            }
        }
        return null;
    }
    
    
}
