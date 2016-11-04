package sugarEmpire;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;

/**
 * 糖域
 * @author xuxuan
 * @date 2016年11月1日
 */
public class SugerDynasty {
    Random r;
    DecimalFormat    df   = new DecimalFormat("######0.00"); 
    /**
     * 距离玄学参数
     */
    private double rangeMetaphysics;
    private int xLength;
    private int yLength;
    /**
     * 糖棋盘
     */
    private int[][] sugerSpace;
    /**
     * 人棋盘
     * true:表示该格有人
     * false:表示该格无人
     */
    private boolean[][] peopleSpace;
    
    /**
     * 糖中心：
     * 越靠近中心地带产糖量和比例越高
     */
    private List<int[]> sugerCenterList;
    
    private double[][] pointValue;
    /**
     * 产生糖
     */
    public void makeSuger(){
        int initSuger=sumSuger();
        for (int i = 0; i < xLength; i++) {
            for (int j = 0; j < yLength; j++) {
                if (!peopleSpace[i][j]) {
                    sugerSpace[i][j]=onePoint(i,j);
                }
            }
        }
        System.out.println("this make more="+(sumSuger()-initSuger));
    }
    public int[] makeMoreSuger(){
        int moreSuger=0;
        int moreSugerPlace=0;
        for (int i = 0; i < xLength; i++) {
            for (int j = 0; j < yLength; j++) {
                if (!peopleSpace[i][j]) {
                    int test=onePoint(i,j);
                    moreSuger+=test;
                    if (sugerSpace[i][j]==0&&test!=0) {
                        moreSugerPlace+=1;
                    }
                    sugerSpace[i][j]+=test;
                }
            }
        }
        int[] ans={moreSuger,moreSugerPlace};
        return ans;
    }
    private int onePoint(int x,int y){
        double sum=pointValue[x][y];
        int ans=0;
        for (int i = 0; i < 4; i++) {
            double test=r.nextDouble();
            if (test<sum) {
                ans++;
            }
        }
        return ans;
    }
    
    /**
     * 糖盘每点的权重
     * @param x
     * @param y
     * @return
     */
    public double onePointValue(int x,int y){
        double sum=0.0;
        for (int i = 0; i < sugerCenterList.size(); i++) {
            int scX=sugerCenterList.get(i)[0];
            int scY=sugerCenterList.get(i)[1];
            double range=Math.pow(x-scX, 2)+Math.pow(y-scY, 2);
            double test=1-1/Math.pow(range+1.1, rangeMetaphysics);
            sum+=1/test;
        }
        return 1-1/sum;
    }
    /**
     * 清空棋盘上的糖
     */
    public void cleanSuger(){
        for (int i = 0; i < xLength; i++) {
            for (int j = 0; j < yLength; j++) {
                sugerSpace[i][j]=0;
            }
        }
    }
    /**
     * 统计棋盘上的糖
     * @return
     */
    public int sumSuger(){
        int sum=0;
        int have=0;
        for (int i = 0; i < xLength; i++) {
            for (int j = 0; j < yLength; j++) {
                sum+=sugerSpace[i][j];
                if (sugerSpace[i][j]>0) {
                    have++;
                }
            }
        }
        return sum;
    }
    
    
    public void createMap(){
        for (int i = 0; i < xLength; i++) {
            for (int j = 0; j < yLength; j++) {
                if (peopleSpace[i][j]) {
                    System.out.print("X ");
                }else{
                    System.out.print((sugerSpace[i][j]!=0?sugerSpace[i][j]:" ")+" ");
                }
            }
            System.out.println();
        }
    }
    
    public void createMap2(){
        char[][] ttt=new char[xLength][yLength];
        for (int i = 0; i < xLength; i++) {
            for (int j = 0; j < yLength; j++) {
                if (peopleSpace[i][j]) {
                    ttt[i][j]='X';
                }else{
                    ttt[i][j]=' ';
                }
            }
        }
        for (int i = 0; i < sugerCenterList.size(); i++) {
            ttt[sugerCenterList.get(i)[0]][sugerCenterList.get(i)[1]]='O';
        }
        for (int i = 0; i < xLength; i++) {
            for (int j = 0; j < yLength; j++) {
                System.out.print(ttt[i][j]+" ");
            }
            System.out.println();
        }
    }
    
    
    
    public SugerDynasty(int x,int y,List<int[]> sugerCenterList,double rangeMetaphysics) {
        this.sugerSpace=new int[x][y];
        this.peopleSpace=new boolean[x][y];
        this.xLength=x;
        this.yLength=y;
        this.sugerCenterList=sugerCenterList;
        this.rangeMetaphysics=rangeMetaphysics;
        this.pointValue=new double[x][y];
        for (int i = -1; i < y; i++) {
            if (i==-1) {
                System.out.print("x ");
            }else{
                System.out.print(i%10+" ");
            }
        }
        System.out.println();
        for (int i = 0; i < x; i++) {
            System.out.print(i%10+" ");
            for (int j = 0; j < y; j++) {
                pointValue[i][j]=onePointValue(i, j);
                System.out.print(((int) (pointValue[i][j]*10))+" ");
            }
            System.out.println();
        }
        this.r=new Random();
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
    public double[][] getPointValue() {
        return pointValue;
    }
    public void setPointValue(double[][] pointValue) {
        this.pointValue = pointValue;
    }
    
}
