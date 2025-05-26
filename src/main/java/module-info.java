module fr.iut.hev.root {
    requires javafx.controls;
    exports fr.iut.hev.root.testing;
    requires javafx.fxml;
    requires jdk.xml.dom;


    opens fr.iut.hev.root.controller to javafx.fxml;
    exports fr.iut.hev.root;
}