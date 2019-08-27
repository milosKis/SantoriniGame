package etf.santorini.km150066d;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MoveObserver {
    private ArrayList<Point> points;
    private static final String directoryName = "Santorini_game";
    private File file = null;


    public MoveObserver() {
        points = new ArrayList<>();
    }

    public void addPoint(Point point) {
        points.add(point);
    }

    public String getString() {
        StringBuilder str = new StringBuilder();
        int size = points.size();
        for (int i = 0; i < size; i++) {
            Point point = points.get(i);
            if ((i == 0) || (i == 2)) {
                str.append(point.getX() + "" + point.getY() + " ");
            }
            else if ((i == 1) || (i == 3)) {
                str.append(point.getX() + "" + point.getY() + "\n");
            }
            else if ((i % 3 == 1) || (i % 3 == 2)) {
                str.append(point.getX() + "" + point.getY() + " ");
            }
            else {
                str.append(point.getX() + "" + point.getY() + "\n");
            }
        }

        return str.toString();
    }

    public void saveToFile() {

        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), directoryName);

            if(!directory.exists() && !directory.isDirectory())
            {
                // create empty directory
                if (directory.mkdirs())
                {
                    Log.i("CreateDir","App dir created");
                }
                else
                {
                    Log.w("CreateDir","Unable to create app dir!");
                }
            }
            else
            {
                Log.i("CreateDir","App dir already exists");
            }

            if (file == null) {
                file = new File(directory, "_" + DateTimeHelper.getCurrentDateTimeString() + ".txt");
                if (!file.exists()) {
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


            byte[] bytes = getString().getBytes();

            try {
                fos.write(bytes);
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void loadFromFile(String filename, Controller controller) {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), directoryName);

            if(!directory.exists() && !directory.isDirectory())
            {
                // create empty directory
                if (directory.mkdirs())
                {
                    Log.i("CreateDir","App dir created");
                }
                else
                {
                    Log.w("CreateDir","Unable to create app dir!");
                }
            }
            else
            {
                Log.i("CreateDir","App dir already exists");
            }


            file = new File(directory, filename);
            if (!file.exists()) {
                    file = null;
                    return;
            }

            try {
                FileInputStream is = new FileInputStream(file);
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);

                String line;
                while ((line = br.readLine()) != null) {
                    String[]  infos = line.split(" ");
                    for (int i = 0; i < infos.length; i++) {
                        int number = Integer.parseInt(infos[i]);
                        int x = number / 10;
                        int y = number % 10;
                        controller.clicked(x, y);
                    }
                }
                is.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
