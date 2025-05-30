module fr.iut.hev.root {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdk.xml.dom;
    requires jdk.javadoc;


    opens fr.iut.hev.root.controller to javafx.fxml;
    exports fr.iut.hev.root;
    exports fr.iut.hev.root.view;
}