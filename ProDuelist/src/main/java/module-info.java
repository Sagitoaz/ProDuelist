module org.example.produelist {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires org.xerial.sqlitejdbc;
    requires java.desktop;
    requires javafx.media;

    opens org.example.produelist to javafx.fxml;
    exports org.example.produelist;

    opens View to javafx.fxml;
    exports View;

    opens Model to javafx.fxml;
    exports Model;

}