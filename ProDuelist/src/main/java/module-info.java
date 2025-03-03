module org.example.produelist {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires org.xerial.sqlitejdbc;
    requires java.desktop;

    opens org.example.produelist to javafx.fxml;
    exports org.example.produelist;

    opens View to javafx.fxml;
    exports View;

}