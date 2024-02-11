package cn.wekyjay.www.wkkit.command;

import cn.wekyjay.www.wkkit.menu.MenuManager;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum TabOpen {
	
    FIRST(Arrays.asList("open"),0,null,new int[]{1}),
	KIT_NAME(Arrays.asList("<KitMenu>"),1,"open",new int[]{2});
	
    private List<String> list;
    private int befPos;
    private String bef;
    private int[] num;
    
    private TabOpen(List<String> list,int befPos, String bef, int[] num){
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
    	if(num[0] == 2)return list = new ArrayList<>(MenuManager.getMenus().keySet());
    	return list;
	}
    public int[] getNum() {
		return num;
	}
    
    
    public static List<String> returnList(String[] Para, int curNum, CommandSender sender) {
        for(TabOpen tab : TabOpen.values() ){
            if(tab.getBefPos()-1 >= Para.length){
                continue;
            }
            if((tab.getBef() == null || tab.getBef().equalsIgnoreCase(Para[tab.getBefPos()-1])) && Arrays.binarySearch(tab.getNum(),curNum)>=0){
            	return tab.getList();
            }
        }
        return null;
    }
    
    
}
