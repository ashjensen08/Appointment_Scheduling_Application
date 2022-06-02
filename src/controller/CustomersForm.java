package controller;

import DAO.CustomerDAO;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Country;
import model.Customer;
import model.Division;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.Optional;
import java.util.ResourceBundle;

import static DAO.CountryDAO.getCountryData;
import static DAO.CustomerDAO.*;
import static DAO.DivisionDAO.getDivsByCountry;


public class CustomersForm implements Initializable {

    public TableView CustTable;
    public TableColumn CustTableId;
    public TableColumn CustTableName;
    public TableColumn CustTableAddress;
    public TableColumn CustTablePostalCode;
    public TableColumn CustTablePhoneNumber;
    public TableColumn CustTableDivId;
    public ComboBox CustCountryIdComboBox;
    public TextField CustAddressField;
    public TextField CustPostalCodeField;
    public ComboBox CustDivIdComboBox;
    public TextField CustPhoneNumberField;
    public TextField CustIdField;
    public TextField CustNameField;
    private Customer customer;
    private int id;
    private String name;
    private String address;
    private String postalCode;
    private String phoneNumber;
    private int countryId;
    private int divId;
    private ObservableList<Country> countryList;
    private ObservableList<Division> divisionList;
    private Country selectedCountry;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Initialized");


        // Insert customer test
        /*
        int rowsAffected = 0;
        try {
            rowsAffected = addCustomer("Ashley", "1234 Weblo Ave", "77445", "123-334-1234",29);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if (rowsAffected > 0) {
            System.out.println("Insert successful");
        }
        else {
            System.out.println("Insert failed");
        }

         */

        // Fill customer table with customer data
        populateCustTable();

        // Lambda function for putting selected customer in Customer Details form
        CustTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                customer = (Customer)newSelection;
                id = customer.getId();
                CustIdField.setText(Integer.toString(id));
                name = customer.getName();
                CustNameField.setText(name);
                address = customer.getAddress();
                CustAddressField.setText(address);
                postalCode = customer.getPostalCode();
                CustPostalCodeField.setText(postalCode);
                phoneNumber = customer.getPhoneNumber();
                CustPhoneNumberField.setText(phoneNumber);
                divId = customer.getDivId();
                /*
                countryId = Customer.findCountryByDiv(divId);
                System.out.println("CountryID " + countryId);
                CustDivIdComboBox.getSelectionModel().select(customer);

                 */
            }
        });


        // TESTS
        // Fill combo boxes
        countryList = getCountryData();
        CustCountryIdComboBox.setVisibleRowCount(5);
        // CustCountryIdComboBox.setPromptText("Country...");
        CustCountryIdComboBox.setItems(countryList);
        // CustCountryIdComboBox.getSelectionModel().selectFirst();

        // Time combo box
        /*
        LocalTime start = LocalTime.of(6, 0);
        LocalTime end = LocalTime.NOON;

        while(start.isBefore(end.plusSeconds(1))) {
            CustCountryIdComboBox.getItems().add(start);
            start = start.plusMinutes(10);
        }
        CustCountryIdComboBox.getSelectionModel().select(LocalTime.of(8, 0));
         */

        // Division by country test
        /*
        ObservableList<Division> divsByCountry = getDivsByCountry(3);
        for (Division d: divsByCountry) {
            System.out.println("Division Name: " + d.getDivName());
            System.out.println("Country ID: " + d.getCountryId());
        }

         */

        // Division list test
        /*
        ObservableList<Division> divList = getDivData();
        for (Division d: divList) {
            System.out.println("Division Name: " + d.getDivId());
            System.out.println("Country Name: " + d.getDivName());
        }

         */

        // Country list test
        /*
        ObservableList<Country> countryList = getCountryData();
        for (Country c: countryList) {
            System.out.println("Country name: " + c.getCountryName());
        }

         */

        // Update customer test
         /*
        int rowsAffected = 0;
        try {
            rowsAffected = CustomerDAO.updateCustomer(4, "Ashley", "5678 Init Road", "84041", "801-345-4567", 103);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if (rowsAffected > 0) {
            System.out.println("Update successful");
        }
        else {
            System.out.println("Update failed");
        }
         */

        // System print table data
        /*ObservableList<Customer> custList = getCustomerList();
        for(Customer C: custList) {
            System.out.println("Customer ID: " + C.getId() + " Name: " + C.getName());
        }*/

        // CustomerDAO.checkDateConversion();

        // Combining two tables in query
        /*
        "SELECT companyID, companyName, countryName FROM company, location WHERE company.locationId = location.locationId";
         */
    }

    private void populateCustTable() {
        // Populate Customer Table on Customers form
        CustTable.setItems(getCustomerData());
        CustTableId.setCellValueFactory(new PropertyValueFactory<>("id"));
        CustTableName.setCellValueFactory(new PropertyValueFactory<>("name"));
        CustTableAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        CustTablePostalCode.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        CustTablePhoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        CustTableDivId.setCellValueFactory(new PropertyValueFactory<>("divId"));
    }

    public void SchedButtonAction(ActionEvent actionEvent) throws IOException {
        // Load Schedule Page
        Parent root = FXMLLoader.load(getClass().getResource("/view/Schedule.fxml"));
        Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 900, 653);
        stage.setTitle("Schedule");
        stage.setScene(scene);
        stage.show();
    }

    public void ReportsButtonAction(ActionEvent actionEvent) throws IOException {
        // Load Schedule Page
        Parent root = FXMLLoader.load(getClass().getResource("/view/Reports.fxml"));
        Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 900, 653);
        stage.setTitle("Reports");
        stage.setScene(scene);
        stage.show();
    }

    public void AddButtonAction(ActionEvent actionEvent) {
        Alert alert;

        // Get new field values
        name = CustNameField.getText();
        address = CustAddressField.getText();
        postalCode = CustPostalCodeField.getText();
        phoneNumber = CustPhoneNumberField.getText();
        divId = ((Division)CustDivIdComboBox.getSelectionModel().getSelectedItem()).getDivId();

        if (addCustomer(name, address, postalCode, phoneNumber, divId) > 0) {
            populateCustTable();
            // Confirm customer added
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Add Customer");
            alert.setContentText("Customer " + name + " has been added.");
            alert.showAndWait();
        }
        else {
            // Alert user: customer has not been added
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Add Customer Error");
            alert.setContentText("Error: Customer has NOT been added");
            alert.showAndWait();
        }
    }

    public void UpdateButtonAction(ActionEvent actionEvent) throws SQLException {
        Alert alert;

        // Get new field values
        name = CustNameField.getText();
        address = CustAddressField.getText();
        postalCode = CustPostalCodeField.getText();
        phoneNumber = CustPhoneNumberField.getText();
        divId = customer.getDivId();

        if (updateCustomer(id, name, address, postalCode, phoneNumber, divId) > 0) {
            // Repopulate table
            populateCustTable();

            // Confirm customer updated
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Update Customer");
            alert.setContentText("Customer " + id + " has been updated.");
            alert.showAndWait();
        }
    }

    public void DeleteButtonAction(ActionEvent actionEvent) throws SQLException {
        Alert alert;

        // Get selected customer from table
        Customer selected = (Customer)CustTable.getSelectionModel().getSelectedItem();

        // If nothing selected, alert user to select customer
        if (selected == null) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Delete Error");
            alert.setContentText("Please select a customer to delete.");
            alert.showAndWait();
            return;
        }
        else {
            // Get customer id
            int custId = selected.getId();

            // If delete is unsuccessful,notify user
            if (deleteCustomer(custId) <= 0) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Delete Error");
                alert.setContentText("Delete unsuccessful.");
                alert.showAndWait();
            }
            else {
                populateCustTable();
            }
        }
    }

    public void ClearButtonAction(ActionEvent actionEvent) {
        CustCountryIdComboBox.getSelectionModel().clearSelection();
        CustDivIdComboBox.getSelectionModel().clearSelection();
        CustDivIdComboBox.getItems().clear();
    }

    public void ExitButtonAction(ActionEvent actionEvent) {
        Alert alert;

        // Confirm user wants to exit program
        alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to exit the program?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            // Close Program
            Platform.exit();
        }
    }

    public void countrySelectedAction(ActionEvent actionEvent) {
        selectedCountry = (Country) CustCountryIdComboBox.getSelectionModel().getSelectedItem();
        if (selectedCountry == null) {
            return;
        }
        int countryId = selectedCountry.getCountryId();
        if (divisionList == null) {
            /*
            selectedCountry = (Country) CustCountryIdComboBox.getSelectionModel().getSelectedItem();
            int countryId = selectedCountry.getCountryId();
             */
            divisionList = getDivsByCountry(countryId);
            CustDivIdComboBox.setVisibleRowCount(5);
            CustDivIdComboBox.setPromptText("First Level Division...");
            CustDivIdComboBox.setItems(divisionList);
        }
        else {
            divisionList.clear();
            /*
            selectedCountry = (Country) CustCountryIdComboBox.getSelectionModel().getSelectedItem();
            int countryId = selectedCountry.getCountryId();
             */
            divisionList = getDivsByCountry(countryId);
            CustDivIdComboBox.setVisibleRowCount(5);
            CustDivIdComboBox.setPromptText("First Level Division...");
            CustDivIdComboBox.setItems(divisionList);
        }

    }
}
