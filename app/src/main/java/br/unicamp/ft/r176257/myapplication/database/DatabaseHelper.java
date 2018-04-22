package br.unicamp.ft.r176257.myapplication.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "EXEMPLO";
    private static final int DB_VERSION = 2;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) { // Vai ser executado apenas na primeira vez em que uma consulta é feita
        // Na etapa de desenvolvimento, onde só eu tenho o app, não precisa criar versões diferentes do BD
        // Só desinstala o apk antigo e instala a nova versão
        db.execSQL("CREATE TABLE Cor " +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Hex TEXT);"
        );
        db.execSQL("CREATE TABLE Categoria " +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Categoria TEXT," +
                "IdCor INTEGER," +
                "FOREIGN KEY(IdCor) REFERENCES Cor(_id));"
        );
        db.execSQL("CREATE TABLE Despesa " +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Data TEXT," +
                "Valor REAL," +
                "IdCategoria INTEGER," +
                "FOREIGN KEY(IdCategoria) REFERENCES Categoria(_id) ON DELETE CASCADE);"
        );
        db.execSQL("INSERT INTO Cor (Hex)" +
                "VALUES (\"#A5A5A5\")," +
                "(\"#EF260B\")," +
                "(\"#D12EA0\")," +
                "(\"#3B15C4\")," +
                "(\"#08A52F\")," +
                "(\"#D1E216\")"
        );
        db.execSQL("INSERT INTO Categoria (Categoria, IdCor)" +
                "VALUES (\"Outras\", 1)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }
}
