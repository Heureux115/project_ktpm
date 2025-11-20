package com.example.demo4.controllers;

import com.example.demo4.Database;
import com.example.demo4.models.Citizen;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class EditCitizenController {

    @FXML private TextField tfName;
    @FXML private TextField tfRelation;
    @FXML private TextField tfDob;
    @FXML private TextField tfCCCD;
    @FXML private TextField tfJob;
    @FXML private Button btnSave;
    @FXML private Button btnCancel;

    private Stage stage;
    private Citizen citizen;
    private Runnable onEditSuccess;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setCitizen(Citizen citizen) {
        this.citizen = citizen;
        tfName.setText(citizen.getFullName());
        tfRelation.setText(citizen.getRelation());
        tfDob.setText(citizen.getDob());
        tfCCCD.setText(citizen.getCccd());
        tfJob.setText(citizen.getJob());
    }

    public void setOnEditSuccess(Runnable onEditSuccess) {
        this.onEditSuccess = onEditSuccess;
    }

    @FXML
    private void handleSave() {
        try (Connection conn = Database.getConnection();
             PreparedStatement st = conn.prepareStatement(
                     "UPDATE citizens SET full_name=?, relation=?, dob=?, cccd=?, job=? WHERE id=?"
             )) {
            st.setString(1, tfName.getText().trim());
            st.setString(2, tfRelation.getText().trim());
            st.setString(3, tfDob.getText().trim());
            st.setString(4, tfCCCD.getText().trim());
            st.setString(5, tfJob.getText().trim());
            st.setInt(6, citizen.getId());
            st.executeUpdate();

            if (onEditSuccess != null) onEditSuccess.run();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancel() {
        stage.close();
    }
}
