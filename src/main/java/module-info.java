module fr.iut.hev.root {
    /*/
    Permet d'accéder aux différents packages pour le jsonReader
     */
    requires javafx.controls;
    requires javafx.fxml;
    requires jamepad;
    requires jdk.xml.dom;
    requires com.fasterxml.jackson.databind;
    exports fr.iut.hev.root.utilities;
    requires com.fasterxml.jackson.core;
    opens fr.iut.hev.root.controller to javafx.fxml;
    exports fr.iut.hev.root;
}