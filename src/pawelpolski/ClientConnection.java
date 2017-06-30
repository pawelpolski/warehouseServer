package pawelpolski;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by macbookpro on 29.06.2017.
 */
public class ClientConnection extends Thread{
    private Model model;
    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private boolean isDone;

    public ClientConnection(Socket socket, Model model){
        try {
            this.socket = socket;
            this.model = model;

            this.output = new ObjectOutputStream(this.socket.getOutputStream());
            this.input = new ObjectInputStream(this.socket.getInputStream());
        }catch(IOException ex){
            System.out.println("CONNECTION FAILED");
        }
    }

    @Override
    public void run() {
        isDone = false;

        try {
            while (!isDone) {
                String message = (String) input.readObject();

                switch(message){
                    case "EXIT":
                        close();
                        break;
                    case "GET_TYPE_LIST":
                        output.writeObject(model.getEquipmentsTypes());
                        break;
                    case "GET_EQUIPMENTS":
                        output.writeObject(model.getEquipments());
                        break;
                    case "POST_EQUIPMENT":
                        Equipment equ = (Equipment) input.readObject();
                        model.addEquipment(equ);
                        break;
                    case "UPDATE_ELEMENT":
                        int index = input.readInt();
                        String text = (String)input.readObject();
                        String text2 = (String)input.readObject();
                        model.updateEquipment(new Equipment(model.getEquipments().get(index).getId(),text, text2,model.getEquipments().get(index).getStatus()));
                        break;
                    case "REMOVE_ELEMENT":
                        int index1 = input.readInt();
                        model.removeEquipment(index1);
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void close(){
        isDone = true;
        try{
            socket.close();
            input.close();
            output.close();
        }catch(IOException ex){

        }
    }
}
