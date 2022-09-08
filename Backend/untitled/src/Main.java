public class Main {
    public static void main(String[] args) {
       // Scanner scanner=new Scanner()


    }
    /*public boolean
    boolean flag=false;
    for(int i=0;i<n/2;i++) {
        if(n==Math.pow(2,i)) {
            flag=true;
            break;
        }
    }
    return */
    boolean flag=false;
    for(int i=2;i<Math.sqrt(n);i++) {
        if(n%i==0) {
            flag=true;
            break;
        }
    }
    if(flag) {
        System.out.println("Not a prime");
    } else

    {
        System.out.println("Its a prime number");
    }
}
