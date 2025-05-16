module universite_paris8.iut.fguerreiromarques.demo {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens universite_paris8.iut.fguerreiromarques.demo to javafx.fxml;
    exports universite_paris8.iut.fguerreiromarques.demo;
}