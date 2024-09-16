module com.baotruongtuan.rdpserver {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;

    opens com.baotruongtuan.rdpserver to javafx.fxml;
    exports com.baotruongtuan.rdpserver;
}