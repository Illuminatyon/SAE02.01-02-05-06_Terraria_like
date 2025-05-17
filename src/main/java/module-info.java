module fr.iut.hev.root {
    requires javafx.controls;
    requires javafx.fxml;
    requires jamepad;
    requires jdk.xml.dom;


    opens fr.iut.hev.root.controller to javafx.fxml;
    exports fr.iut.hev.root;
}