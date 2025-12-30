package com.example.demo4.controllers;

import com.example.demo4.Main;
import com.example.demo4.dao.CitizenDao;
import com.example.demo4.models.Citizen;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Label;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

public class StatisticsController extends BaseController {

    @FXML private Label lblTotalCitizens;
    @FXML private Label lblMale;
    @FXML private Label lblFemale;

    // Biểu đồ độ tuổi
    @FXML private BarChart<String, Number> chartAge;
    @FXML private CategoryAxis xAxisAge;
    @FXML private NumberAxis yAxisAge;

    // Biểu đồ giới tính
    @FXML private PieChart chartGender;

    @FXML
    public void initialize() {
        loadStatistics();
    }

    @FXML
    private void loadStatistics() {
        try {
            List<Citizen> allCitizens = CitizenDao.findAll();

            // ===== TỔNG SỐ =====
            lblTotalCitizens.setText(String.valueOf(allCitizens.size()));

            int male = 0, female = 0, unknown = 0;

            // ===== ĐỘ TUỔI =====
            int mamNon = 0;
            int mauGiao = 0;
            int cap1 = 0;
            int cap2 = 0;
            int cap3 = 0;
            int laoDong = 0;
            int nghiHuu = 0;

            LocalDate now = LocalDate.now();

            for (Citizen c : allCitizens) {

                // ===== GIỚI TÍNH (dựa CCCD) =====
                String cccd = c.getCccd();
                if (cccd != null && cccd.length() == 12) {
                    char genderDigit = cccd.charAt(3);
                    if (genderDigit == '0' || genderDigit == '2' ||
                            genderDigit == '4' || genderDigit == '6' ||
                            genderDigit == '8') {
                        male++;
                    } else {
                        female++;
                    }
                } else {
                    unknown++;
                }

                // ===== TUỔI =====
                if (c.getDob() != null) {
                    int age = Period.between(c.getDob(), now).getYears();

                    if (age <= 3) mamNon++;
                    else if (age <= 5) mauGiao++;
                    else if (age <= 10) cap1++;
                    else if (age <= 14) cap2++;
                    else if (age <= 17) cap3++;
                    else if (age <= 59) laoDong++;
                    else nghiHuu++;
                }
            }

            lblMale.setText(String.valueOf(male));
            lblFemale.setText(String.valueOf(female));

            // ===== BIỂU ĐỒ ĐỘ TUỔI =====
            XYChart.Series<String, Number> ageSeries = new XYChart.Series<>();
            ageSeries.setName("Số người");

            ageSeries.getData().add(new XYChart.Data<>("Mầm non (0-3)", mamNon));
            ageSeries.getData().add(new XYChart.Data<>("Mẫu giáo (4-5)", mauGiao));
            ageSeries.getData().add(new XYChart.Data<>("Cấp 1 (6-10)", cap1));
            ageSeries.getData().add(new XYChart.Data<>("Cấp 2 (11-14)", cap2));
            ageSeries.getData().add(new XYChart.Data<>("Cấp 3 (15-17)", cap3));
            ageSeries.getData().add(new XYChart.Data<>("Lao động (18-59)", laoDong));
            ageSeries.getData().add(new XYChart.Data<>("Nghỉ hưu (60+)", nghiHuu));

            chartAge.getData().clear();
            chartAge.getData().add(ageSeries);

            // ===== BIỂU ĐỒ GIỚI TÍNH =====
            chartGender.getData().clear();

            if (male > 0)
                chartGender.getData().add(new PieChart.Data("Nam (" + male + ")", male));

            if (female > 0)
                chartGender.getData().add(new PieChart.Data("Nữ (" + female + ")", female));

            if (unknown > 0)
                chartGender.getData().add(new PieChart.Data("Không rõ (" + unknown + ")", unknown));

        } catch (Exception e) {
            e.printStackTrace();
            showError("Lỗi", "Không thể tải thống kê: " + e.getMessage());
        }
    }

    @FXML
    private void backToMenu() throws Exception {
        Main.showMenu();
    }
}
