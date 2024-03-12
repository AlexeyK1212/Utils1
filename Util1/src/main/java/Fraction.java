




interface Fractionable {




    @Cache(1000)
    double doubleValue();
    @Mutator
    void setNum(int num);
    @Mutator
    void setDenum(int denum);

    int getNum();

}



public class Fraction implements Fractionable {


    private int num;
    private int denum;

    public Fraction(int num, int denum) {
        this.num = num;
        this.denum = denum;

    }

    public void setNum(int num) {
        this.num = num;
    }

    public void setDenum(int denum) {
        this.denum = denum;

    }


    public int getNum() {
        return this.num;

    }



    @Override
    public double doubleValue() {
        System.out.println("invoke double value") ;
        return (double) num/denum;
    }



}
