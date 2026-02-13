/**
 * Commmand Line Utility
 */
public class TopSecret {
    public static void main(String[]args){
        try{
            UserInterface UI = new UserInterface(args);
            if(args.length ==1){
                System.out.println(UI.transferFileSelected());
            }
            else if(args.length == 2){
                System.out.println(UI.transferFileSelectedWithAltKey());
            }else if (args.length ==0){
                System.out.println(UI.transfer());
            }
            else{
                System.out.println("Invalid number of arguments.");
            }
        }
        catch(Exception e){
            System.out.println("An error occured." + e.getMessage());
        }
    }
}
