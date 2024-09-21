module com.baotruongtuan.rdpserver {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires Java.WebSocket;

    opens com.baotruongtuan.rdpserver to javafx.fxml;
    exports com.baotruongtuan.rdpserver;
    exports com.baotruongtuan.rdpserver.controller;
    opens com.baotruongtuan.rdpserver.controller to javafx.fxml;
}