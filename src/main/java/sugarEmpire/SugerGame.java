package sugarEmpire;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class SugerGame {
    Random r;
    public static void main(String[] args) {
        long l=new Date().getTime();
        int x=50;
        int y=50;
        int sugerCenterNum=2;
        int peopleNum=200;
        int round=1000;
        new SugerGame().startGame(x, y, sugerCenterNum, peopleNum,round);
        System.out.println("棋盘规格："+x+"*"+y+";糖人数量："+peopleNum+";回合数："+round+";耗时："+(new Date().getTime()-l));
    }
    public void startGame(int x,int y,int sugerCenterNum,int peopleNum,int round){
        r=new Random();
        List<int[]> sugerCenterList=makeSugerCenter(x,y,sugerCenterNum);
        /**
         * 距离玄学参数
         */
        double rangeMetaphysics=0.2;
        SugerDynasty sugerDynasty=new SugerDynasty(x, y, sugerCenterList,rangeMetaphysics);
        int[][] sugerSpace=sugerDynasty.getSugerSpace();
        boolean[][] peopleSpace=sugerDynasty.getPeopleSpace();
        /**
         * 消耗玄学参数
         * consume=consumeMetaphysics+rangdom(1-consumeMetaphysics)
         */
        int[] sightNum=new int[6];
        double consumeMetaphysics=0.5;
        double baseHealth=10;
        double addHealth=20;
        int baseSightLength=1;
        int addSightLength=6;
        List<SugerPeople> peopleList=new ArrayList<SugerPeople>();
        int[] nums=new int[x*y];
        for (int i = 0; i < nums.length; i++) {
            nums[i]=i;
        }
        int[] initValue=new int[10];
        for (int i = 0; i < peopleNum; i++) {
            double consume=(consumeMetaphysics+r.nextDouble()*(1-consumeMetaphysics))/8;
            double initHealth=baseHealth+r.nextDouble()*addHealth;
            int test=r.nextInt(x*y-i);
            int initX=nums[test]/y;
            int initY=nums[test]%y;
            //获得出生地价值
            int value=(int) (sugerDynasty.getPointValue()[initX][initX]*10);
            try {
                initValue[value]++;
            } catch (Exception e) {
                System.out.println("value="+value);
            }
            int addSight=r.nextInt(addSightLength);
            sightNum[addSight]++;
            int sightLength=baseSightLength+addSight;
            SugerPeople sp=new SugerPeople(i,initX, initY,sightLength, initHealth, consume, sugerSpace, peopleSpace);
            nums[test]=nums[x*y-i-1];
            peopleList.add(sp);
        }
        sugerDynasty.makeSuger();
//        sugerDynasty.createMap();
        for (int i = 0; i < sugerCenterList.size(); i++) {
            System.out.print("("+sugerCenterList.get(i)[0]+","+sugerCenterList.get(i)[1]+")\t");
        }
        System.out.println();
        int more=sugerDynasty.sumSuger();
        System.out.println("init!!!!");
        double sumConsume=0;
//        for (int i = 0; i < round; i++) {
//            for (int j = 0; j < peopleList.size(); j++) {
//                if (peopleList.get(j).isLive()) {
//                    sumConsume+=peopleList.get(j).getConsume();
//                }
//                peopleList.get(j).nextDo();
//            }
//            if (i%10==9) {
//                int[] ans=sugerDynasty.makeMoreSuger();
//                System.out.println("turn "+(i+1)+" moreSugerNum="+ans[0]+"\tmoreSugerPlace="+ans[1]+"\tsumConsume="+sumConsume);
//                sumConsume=0;
//            }
//            if ((i+1)%50==0) {
//                calculation(peopleList,i+1);
////                sugerDynasty.createMap2();
//            }
//        }
//        createMapConsume(peopleList, sugerCenterList, x, y);
//        System.out.println("————————————上图为消耗——————————————————————————————————————————————————————————————————————下图为眼光————————————————————————————————————");
//        createMapSight(peopleList, sugerCenterList, x, y);
//        System.out.println("————————————上图为眼光——————————————————————————————————————————————————————————————————————下图为糖果————————————————————————————————————");
//        sugerDynasty.createMap();
//        System.out.println("——————————————————————————————————————————————————————————————————————————————————————————————————————————");
        int[] finalLiveSight=new int[6];
        int[] finalLiveValue=new int[10];
        for (int i = 0; i < peopleList.size(); i++) {
            SugerPeople sp=peopleList.get(i);
            if (sp.isLive()) {
                finalLiveSight[sp.getSightLength()-1]++;
              //获得出生地价值
                int value=(int) (sugerDynasty.getPointValue()[sp.getInitX()][sp.getInitY()]*10);
                initValue[value]++;
            }
        }
        for (int i = 0; i < 6; i++) {
            System.out.print(sightNum[i]+"\t");
        }
        System.out.println();
        for (int i = 0; i < 6; i++) {
            System.out.print(finalLiveSight[i]+"\t");
        }
        System.out.println();
        for (int i = 0; i < 10; i++) {
            System.out.print(initValue[i]+"\t");
        }
        System.out.println();
        for (int i = 0; i < 10; i++) {
            System.out.print(finalLiveValue[i]+"\t");
        }
        System.out.println();
    }
    
    public List<int[]> makeSugerCenter(int x,int y,int sugerCenterNum){
        List<int[]> list=new ArrayList<int[]>();
        for (int i = 0; i < sugerCenterNum; i++) {
            int[] sugerCenter=new int[2];
            sugerCenter[0]=r.nextInt(x);
            sugerCenter[1]=r.nextInt(y);
            System.out.print("("+sugerCenter[0]+","+sugerCenter[1]+")\t");
            list.add(sugerCenter);
        }
        System.out.println();
        return list;
    }
    
    public void calculation(List<SugerPeople> list,int turnId){
        int live=0;
        double sumHealth=0;
        List<Double> healthList=new ArrayList<Double>();
        for (int i = 0; i < list.size(); i++) {
            SugerPeople sp=list.get(i);
            if (sp.isLive()) {
                live++;
                sumHealth+=sp.getCurrentHealth();
                healthList.add(sp.getCurrentHealth());
            }
        }
        double[] healthNums=new double[healthList.size()];
        for (int i = 0; i < healthNums.length; i++) {
            healthNums[i]=healthList.get(i);
        }
        Arrays.sort(healthNums);
        System.out.println("turn "+turnId+" liveNum="+live+"\tliveSumHealth="+((int)sumHealth)+"\tliveAvgHealth="+((int)(sumHealth/live)));
        System.out.print("top 10: ");
        for (int i = 0; i < 10; i++) {
            System.out.print((int)healthNums[healthNums.length-i-1]+"\t");
        }
        System.out.println();
    }
    
    
    public void createMapConsume(List<SugerPeople> peopleList,List<int[]> sugerCenterList,int x,int y){
        char[][] ttt=new char[x][y];
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                ttt[i][j]=' ';
            }
        }
        for (int i = 0; i < peopleList.size(); i++) {
            SugerPeople sp=peopleList.get(i);
            if (sp.isLive()) {
                ttt[sp.getCurrentX()][sp.getCurrentY()]=(char) ('0'+((int)(sp.getConsume()*100)));
            }
        }
        for (int i = 0; i < sugerCenterList.size(); i++) {
            ttt[sugerCenterList.get(i)[0]][sugerCenterList.get(i)[1]]='@';
        }
        
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                System.out.print(ttt[i][j]+" ");
            }
            System.out.println();
        }
    }
    
    public void createMapSight(List<SugerPeople> peopleList,List<int[]> sugerCenterList,int x,int y){
        char[][] ttt=new char[x][y];
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                ttt[i][j]=' ';
            }
        }
        for (int i = 0; i < peopleList.size(); i++) {
            SugerPeople sp=peopleList.get(i);
            if (sp.isLive()) {
                ttt[sp.getCurrentX()][sp.getCurrentY()]=(char) ('0'+sp.getSightLength());
            }
        }
        for (int i = 0; i < sugerCenterList.size(); i++) {
            ttt[sugerCenterList.get(i)[0]][sugerCenterList.get(i)[1]]='@';
        }
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                System.out.print(ttt[i][j]+" ");
            }
            System.out.println();
        }
    }
}
