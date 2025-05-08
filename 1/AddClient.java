import java.rmi.Naming;

public class AddClient {
    public static void main(String args[]) {
        if (args.length < 3) {
            System.out.println("Usage: java AddClient <server_ip> <num1> <num2>");
            return;
        }

        try {
            String addServerURL = "//" + args[0] + "/AddServer";
            AddServerIntf addServerIntf = (AddServerIntf) Naming.lookup(addServerURL);

            System.out.println("The first number is: " + args[1]);
            double d1 = Double.parseDouble(args[1]);

            System.out.println("The second number is: " + args[2]);
            double d2 = Double.parseDouble(args[2]);

            System.out.println("The sum is: " + addServerIntf.add(d1, d2));
        } catch (Exception e) {
            System.out.println("Exception in main: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
// 1 terminal javac *.java
//2 termiinal rmiregistry
//1 termianl java AddServer
// 3 terminal java AddClient  192.168.1.100 1 2
////java AddClient localhost 5 7



//rmic AddServerImpl
