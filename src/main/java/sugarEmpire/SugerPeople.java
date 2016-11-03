package sugarEmpire;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 糖人
 * @author xuxuan
 * @date 2016年11月1日
 */
public class SugerPeople {
    /**
     * id
     */
    private int id;
    private int xLength;
    private int yLength;
    /**
     * 初始x
     */
    private int initX;
    /**
     * 初始y
     */
    private int initY;
    /**
     * 初始体力
     */
    private double initHealth;
    
    /**
     * 体力消耗
     */
    private double consume;
    
    /**
     * 视野
     */
    private int sightLength;
    
    
    
    /**
     * 当前x
     */
    private int currentX;
    /**
     * 当前y
     */
    private int currentY;
    
    /**
     * 当前体力
     */
    private double currentHealth;
    
    /**
     * 存活状态
     */
    private boolean live;
    
    /**
     * 糖棋盘
     */
    private int[][] sugerSpace;
    
    /**
     * 人棋盘
     */
    private boolean[][] peopleSpace;
    
    /**
     * 随机因子
     */
    private Random r;
    public SugerPeople(int id,int initX, int initY,int sightLength, double initHealth, double consume,  int[][] sugerSpace, boolean[][] peopleSpace) {
        super();
        this.id=id;
        this.initX = initX;
        this.initY = initY;
        this.initHealth = initHealth;
        this.consume = consume;
        this.currentX = initX;
        this.currentY = initY;
        this.currentHealth = initHealth;
        this.live = true;
        this.sugerSpace = sugerSpace;
        this.peopleSpace = peopleSpace;
        this.xLength=sugerSpace.length;
        this.yLength=sugerSpace[0].length;
        this.sightLength=sightLength;
        this.peopleSpace[initX][initY]=true;
        r=new Random();
    }
    /**
     * 糖人下一步行动
     * 目前策略是先寻找视野内最大的糖块，如果有相同大的则取近的
     * 然后朝目标移动的过程中，逐步选取次大的点
     */
    public void nextDo(){
        if (live) {
            int max=0;
            int count=0;
            int targetX=currentX+(r.nextInt(2)==0?1:-1);
            int targetY=currentY+(r.nextInt(2)==0?1:-1);
            int targetLength=Integer.MAX_VALUE;
            /**
             * 寻找视野内最大的目标
             */
            for (int i = Math.max(0, currentX-sightLength); i <= Math.min(xLength-1, currentX+sightLength); i++) {
                for (int j = Math.max(0, currentY-sightLength); j <= Math.min(xLength-1, currentY+sightLength); j++) {
                    if (sugerSpace[i][j]>max) {
                        max=sugerSpace[i][j];
                        targetX=i;
                        targetY=j;
                        count=1;
                        targetLength=Math.abs(i-currentX)+Math.abs(j-currentY);
                    }else if (sugerSpace[i][j]==max) {
                        int length=Math.abs(i-currentX)+Math.abs(j-currentY);
                        if (length<targetLength) {
                            targetX=i;
                            targetY=j;
                            count=1;
                            targetLength=Math.abs(i-currentX)+Math.abs(j-currentY);
                        }else if(length==targetLength){
                            count+=1;
                            if (r.nextInt(count)==0) {
                                targetX=i;
                                targetY=j;
                            }
                        }
                    }
                }
            }
            /**
             * 朝着目标前进
             */
            toTarget(targetX,targetY);
        }
    }
    public void toTarget(int targetX,int targetY){
        if (targetX==currentX) {
            if ((targetY>currentY&&!peopleSpace[currentX][currentY+1])||(targetY<currentY&&!peopleSpace[currentX][currentY-1])) {
                move(currentX,currentY+(targetY>currentY?1:-1));
            }else{
                moveRandom();
            }
        }else if (targetY==currentY) {
            if ((targetX>currentX&&!peopleSpace[currentX+1][currentY])||(targetX<currentX&&!peopleSpace[currentX-1][currentY])) {
                move(currentX+(targetX>currentX?1:-1),currentY);
            }else{
                moveRandom();
            }
        }else{
            int xf=targetX>currentX?1:-1;
            int yf=targetY>currentY?1:-1;
            int max=0;
            int count=0;
            int t=r.nextInt(2);
            int newTargetX=currentX+(t==0?xf:0);
            int newTargetY=currentY+(t==0?0:yf);
            int targetLength=Integer.MAX_VALUE;
            for (int i = currentX; i <= targetX; i+=xf) {
                for (int j = currentY; j <= targetY; j+=yf) {
                    if (i==currentX&&j==currentY) {
                        continue;
                    }
                    if (i==targetX&&j==targetY) {
                        break;
                    }
                    if (sugerSpace[i][j]>max) {
                        max=sugerSpace[i][j];
                        newTargetX=i;
                        newTargetY=j;
                        count=1;
                        targetLength=Math.abs(i-currentX)+Math.abs(j-currentY);
                    }else if (sugerSpace[i][j]==max) {
                        int length=Math.abs(i-currentX)+Math.abs(j-currentY);
                        if (length<targetLength) {
                            newTargetX=i;
                            newTargetY=j;
                            count=1;
                            targetLength=Math.abs(i-currentX)+Math.abs(j-currentY);
                        }else if(length==targetLength){
                            count+=1;
                            if (r.nextInt(count)==0) {
                                newTargetX=i;
                                newTargetY=j;
                            }
                        }
                    }
                }
            }
            toTarget(newTargetX,newTargetY);
        }
    }
    public void move(int x,int y){
        //修改棋盘
        peopleSpace[currentX][currentY]=false;
        peopleSpace[x][y]=true;
        //修改人物血量
        currentHealth+=sugerSpace[x][y];
        currentHealth-=consume;
        //修改糖棋盘
        sugerSpace[x][y]=0;
        //修改人物坐标
        currentX=x;
        currentY=y;
        if (currentHealth<0) {
            live=false;
            peopleSpace[x][y]=false;
        }
    }
    public void moveRandom(){
        List<Integer> list=new ArrayList<Integer>();
        for (int i = 0; i < 4; i++) {
            int xf=i/2;
            int yf=i%2;
            int nx=currentX+1-xf*2;
            int ny=currentY+1-yf*2;
            if (nx>=0&&nx<xLength&&ny>=0&&ny<yLength&&!peopleSpace[nx][ny]) {
                list.add(i);
            }
        }
        if (list.size()==0) {
            move(currentX, currentY);
        }else{
            int t=r.nextInt(list.size());
            int xf=list.get(t)/2;
            int yf=list.get(t)%2;
            int nx=currentX+1-xf*2;
            int ny=currentY+1-yf*2;
            move(nx, ny);
        }
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getxLength() {
        return xLength;
    }
    public void setxLength(int xLength) {
        this.xLength = xLength;
    }
    public int getyLength() {
        return yLength;
    }
    public void setyLength(int yLength) {
        this.yLength = yLength;
    }
    public int getInitX() {
        return initX;
    }
    public void setInitX(int initX) {
        this.initX = initX;
    }
    public int getInitY() {
        return initY;
    }
    public void setInitY(int initY) {
        this.initY = initY;
    }
    public double getInitHealth() {
        return initHealth;
    }
    public void setInitHealth(double initHealth) {
        this.initHealth = initHealth;
    }
    public double getConsume() {
        return consume;
    }
    public void setConsume(double consume) {
        this.consume = consume;
    }
    public int getSightLength() {
        return sightLength;
    }
    public void setSightLength(int sightLength) {
        this.sightLength = sightLength;
    }
    public int getCurrentX() {
        return currentX;
    }
    public void setCurrentX(int currentX) {
        this.currentX = currentX;
    }
    public int getCurrentY() {
        return currentY;
    }
    public void setCurrentY(int currentY) {
        this.currentY = currentY;
    }
    public double getCurrentHealth() {
        return currentHealth;
    }
    public void setCurrentHealth(double currentHealth) {
        this.currentHealth = currentHealth;
    }
    public boolean isLive() {
        return live;
    }
    public void setLive(boolean live) {
        this.live = live;
    }
    public int[][] getSugerSpace() {
        return sugerSpace;
    }
    public void setSugerSpace(int[][] sugerSpace) {
        this.sugerSpace = sugerSpace;
    }
    public boolean[][] getPeopleSpace() {
        return peopleSpace;
    }
    public void setPeopleSpace(boolean[][] peopleSpace) {
        this.peopleSpace = peopleSpace;
    }
    public Random getR() {
        return r;
    }
    public void setR(Random r) {
        this.r = r;
    }
    @Override
    public String toString() {
        return "SugerPeople [id=" + id + ", initX=" + initX + ", initY=" + initY + ", initHealth=" + initHealth
                + ", consume=" + consume + ", sightLength=" + sightLength + ", currentHealth=" + currentHealth + "]";
    }
    
}
