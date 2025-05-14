module fr.iut.hev.terraria.terraria {
    requires javafx.controls;
    requires javafx.fxml;
    //requires discord.rpc;
    requires javafx.media;
    requires jamepad;


    //opens fr.iut.hev.terraria.terraria to javafx.fxml;
    opens fr.iut.hev.terraria.terraria.controller to javafx.fxml;
    exports fr.iut.hev.terraria.terraria;
    exports fr.iut.hev.terraria.terraria.controller;
}