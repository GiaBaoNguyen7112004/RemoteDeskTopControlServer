module com.baotruongtuan.rdpclient {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires Java.WebSocket;

    opens com.baotruongtuan.rdpclient to javafx.fxml;
    exports com.baotruongtuan.rdpclient;
}