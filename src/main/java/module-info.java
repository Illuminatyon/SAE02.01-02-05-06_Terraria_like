module fr.iut.hev.root {
    requires javafx.fxml;
    requires jdk.xml.dom;
    requires jdk.javadoc;
    requires org.controlsfx.controls;
    requires com.google.gson;
    requires org.jetbrains.annotations;
    requires org.hildan.fxgson;


    opens fr.iut.hev.root.controller to javafx.fxml;
    opens fr.iut.hev.root.model to com.google.gson;
    opens fr.iut.hev.root.model.enums to com.google.gson;
    exports fr.iut.hev.root;
    exports fr.iut.hev.root.view;
    opens fr.iut.hev.root.utils to com.google.gson;
}