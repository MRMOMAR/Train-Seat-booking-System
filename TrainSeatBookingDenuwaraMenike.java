import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;



public class TrainSeatBookingDenuwaraMenike extends Application {
    static final int NUM_SEATS = 42;     //Global variable assigning number of seats to 42
    List<Button> seats = new ArrayList<>();    //Creating an array list for the seats numbers
    public static List<Button> reserved = new ArrayList<>();  //Creating an array list for the reserved seat
    List<String> names = new ArrayList<>();      //Creating an arraylist for names of customers

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) {

        for(int i = 0, c = 0, r = 0; i < NUM_SEATS; i++) {   //Making seats for the GUI
            Button seat = new Button("" + (i+1));
            seat.setMaxSize(100, 100);
            seat.setMinSize(40, 40);
            seat.setStyle("-fx-font-size: 15px; -fx-font-family: 'Clear Sans';-fx-background-color: #dddd;");
            if(i % 11== 0) {
                r +=1;
                c = 0;
            }
            GridPane.setConstraints(seat, c, r);
            seats.add(seat);
            c++;
        } menu();
    }

    public Label makeLabel() {    //Making label which defines the process
        Label lbl = new Label();
        lbl.setTextFill(Color.web("blue"));
        GridPane.setConstraints(lbl, 0, 0);
        GridPane.setColumnSpan(lbl,6);
        return lbl;
    }
    public GridPane makeGrid() {   // Grid pane aliging its child components a grid
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(15);
        grid.setHgap(15);
        return grid;
    }

    public void menu() { //Main menu containing the from which the functions are called
        Scanner sc = new Scanner(System.in);
        List<String> options = new ArrayList<>();
        options.add("A");options.add("V");options.add("E");options.add("D");options.add("F");
        options.add("S");options.add("L");options.add("O");options.add("Q");

        System.out.println("----------------Main Menu--------------\n"+
                "Enter A to add a customer \n"+
                "Enter V to view all seat \n"+
                "Enter E to view empty seats \n"+
                "Enter D to delete booked seat \n"+
                "Enter F to find a seat by customer \n"+
                "Enter L to load program\n"+
                "Enter O to View Seats in name Order\n"+
                "Enter Q to quit Program \n");

        System.out.println("Enter required input:");
        String option = sc.nextLine();

        if (options.contains(option.toUpperCase())) {   //switch case to input required function from user
            switch (option.toUpperCase()) {
                case "A":
                    addCustomer();
                    break;
                case "E":
                    viewEmptySeats();
                    break;
                case "V":
                    viewAllSeats();
                    break;
                case "D":
                    deleteBookedSeats();
                    break;
                case "F":
                    findSeatsByName();
                    break;
                case "O":
                    nameSortOrder(names);
                    displayName();
                    break;
                case "L":
                    loadProgram();
                    break;
                case "Q":                 //Quit/End program
                    Platform.exit();
                    break;
            }
        } else {//continously prompt user for input unless any of above inputs are obtained
            System.out.println("Invalid Input");
            menu();
        }
    }
    public void viewAllSeats() {    //View seats other than the ones are booked
        Stage stage = new Stage();

        GridPane grid = makeGrid();
        Label lbl = makeLabel();

        lbl.setText("View All Seats");
        GridPane.setConstraints(lbl, 0, 0);
        GridPane.setColumnSpan(lbl,6);


        Button quit = new Button("Quit");  //creating the quit button
        quit.setMinWidth(80);
        quit.setMaxWidth(150);
        GridPane.setConstraints(quit, 5, 8);
        GridPane.setColumnSpan(quit, 2);
        grid.getChildren().add(quit);

        grid.getChildren().add(lbl);
        for(Button seat : seats) {
            seat.setOnAction(null);
            if(!reserved.contains(seat)) {
                seat.setStyle("-fx-background-color: #5cf4ff");   //Preview light blue color button of all unbooked seat
            } else {
                seat.setStyle("-fx-background-color: #ff4d60");   //Preview red color button of the seats that are booked
            }
            grid.getChildren().add(seat);
        }
        Scene scene = new Scene(grid, 700, 500); //Create scene for view seat gui
        stage.setScene(scene);
        stage.setTitle("Denuwara Menike Train");
        stage.show();
        quit.setOnAction(e -> {         //Program runs on the console after pressing Quit button on  GUI
            stage.close();
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    menu();
                }
            });
        });
    }

    public void addCustomer() {  //Add customer to the required seat
        //set stage and buttons
        Stage stage = new Stage();

        Label lbl = makeLabel();
        GridPane grid = makeGrid();
        lbl.setText("Book a seat");
        grid.getChildren().add(lbl);

        TextField name = new TextField();
        GridPane.setConstraints(name, 0, 8);
        GridPane.setColumnSpan(name, 5);
        grid.getChildren().add(name);

        Button enter = new Button("Enter");
        GridPane.setConstraints(enter, 9, 8);
        grid.getChildren().add(enter);

        Button quit = new Button("Quit");
        GridPane.setConstraints(quit, 10, 8);
        grid.getChildren().add(quit);

        final Button[] toBeReserved = {null};
        for(Button seat : seats) {
            if(reserved.contains(seat)) {
                seat.setOnAction(null);
                seat.setStyle("-fx-background-color: #ff9ca7");  //booked seat preview
            } else {
                seat.setStyle("");
                seat.setOnAction(e ->  {
                    if(toBeReserved[0] != null){
                        toBeReserved[0].setStyle("");
                    }
                    if( toBeReserved[0] != seat) {
                        seat.setStyle("-fx-background-color: #5cf4ff");  //selected seat to book preview
                        toBeReserved[0] = seat;
                    }
                    else{ seat.setStyle("");
                        toBeReserved[0] = null;}  //seat unselected
                });
            }
            grid.getChildren().add(seat);
            // make seats reservable/add seats

        }
        Scene scene = new Scene(grid, 700, 500);
        stage.setScene(scene);
        stage.setTitle("Denuwara Menike Train");
        stage.show();
        enter.setOnAction(e -> {
            if(toBeReserved[0] != null){
                if(!(name.getText().trim().isEmpty()) || !(name.getText().length() != 1)) { // Avoiding in obtaining  q as a name
                    name.setStyle("-fx-border-color: silver");
                    reserved.add(toBeReserved[0]);
                    names.add(name.getText());
                    toBeReserved[0] = null;
                    stage.close();
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            menu();
                        }
                    });
                }
            }
        });
        quit.setOnAction(e -> {  //Program runs in console quitting the gui
            toBeReserved[0] = null;
            stage.close();
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    menu();
                }
            });
        });
    }
    public void viewEmptySeats() {   //Unbooked available seats
        Stage stage = new Stage();
        GridPane grid = makeGrid();
        Label lbl = makeLabel();
        Button quit = new Button("Quit");
        quit.setMinWidth(80);
        quit.setMaxWidth(150);
        GridPane.setConstraints(quit, 5, 8);
        GridPane.setColumnSpan(quit, 2);
        grid.getChildren().add(quit);

        int i = 0; // to count reserved seats
        for(Button seat : seats) {
            if(!reserved.contains(seat)) {
                seat.setOnAction(null);
                seat.setStyle("");
                grid.getChildren().add(seat);
                i++;
            }
        }
        // different label when all seats are booked
        if(i > 0) {
            lbl.setText("View Empty Seats");
            lbl.setStyle("-fx-font-size: 18px");
        } else {
            lbl.setText("Sorry, All Seats Are Booked");
            lbl.setStyle("-fx-font-size: 26px");
        }
        GridPane.setConstraints(lbl, 0, 0);
        GridPane.setColumnSpan(lbl,6);
        grid.getChildren().add(lbl);

        Scene scene = new Scene(grid, 700, 500);
        stage.setScene(scene);
        stage.setTitle("Denuwara Menike Train");
        stage.show();
        quit.setOnAction(e -> {
            stage.close();
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    menu();
                }
            });
        });
    }
    public void deleteBookedSeats() {   //removing a booked seat by name
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter your name: "); //prompt customer name of booked seat
        String getName = sc.nextLine();

        if(!getName.toLowerCase().equals("q")) { //to quit from incorrect name entering to the main menu
            if (names.indexOf(getName) != -1) {  //if name match name in array remove/delete the entry
                reserved.remove(names.indexOf(getName));
                names.remove(getName);
                System.out.println("customer name "+getName+" your seat is successfully removed");
                menu();
            } else {
                System.out.println("No seat booked for the provided name "+getName);  //when customer name doesn't match name in array
                deleteBookedSeats();
            }
        } else {
            menu();
        }

    }

    public void findSeatsByName() {     //find seat number by customer name
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter your name: ");
        String getName = sc.nextLine();

        if(!getName.toLowerCase().equals("q")) {  //exit to main menu on input of q
            if (names.indexOf(getName) != -1) {
                String seat = reserved.get(names.indexOf(getName)).getText();
                System.out.println("****"+getName+" your seat no is: " + seat+"****\n");
            } else {
                System.out.println("Please consider that provided name "+getName+ " has no seats booked!");  //input name doesn't match available booked names
                findSeatsByName();
            }
        }else{  //To main menu
            menu();}
    }
    public static List<String> nameSortOrder(List <String> names){  //Sorting booked cutomer names  inalphabetical order
        String temp;
        Button temp2;
        for (int i = 0; i < names.size() - 1; i++) {
            for (int j = 1; j < names.size() - i; j++) {
                if (names.get(j - 1).compareTo(names.get(j)) > 0) {
                    temp = names.get(j - 1); //sorting name array
                    names.set(j - 1, names.get(j));
                    names.set(j, temp);

                    temp2 = reserved.get(j - 1); //sorting reserved array
                    reserved.set(j - 1, reserved.get(j));
                    reserved.set(j, temp2);
                }

            }
//
        }
        return(names);



    }
    public void displayName(){  // display sort names
        for (int i=0;i <names.size();i++){
            String customerName=names.get(i);
            String seatNumber=reserved.get(i).getText();
            System.out.println("Customer "+customerName+" your seat no is: "+seatNumber);
        }

    }

    public void loadProgram(){
        menu();
    }
}



