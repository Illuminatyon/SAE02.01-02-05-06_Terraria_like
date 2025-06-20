module fr.iut.hev.root {
    requires javafx.fxml;
    requires jdk.xml.dom;
    requires jdk.javadoc;
    requires java.desktop;
    requires java.sql;
    requires org.controlsfx.controls;


    opens fr.iut.hev.root.controller to javafx.fxml;
    exports fr.iut.hev.root;
    exports fr.iut.hev.root.view;
    exports fr.iut.hev.root.view.actor;
}