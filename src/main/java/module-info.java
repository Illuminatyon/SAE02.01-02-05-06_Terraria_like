module fr.iut.hev.root {
    requires javafx.controls;
    requires javafx.fxml;


    opens fr.iut.hev.root to javafx.fxml;
    exports fr.iut.hev.root;
}