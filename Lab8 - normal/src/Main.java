public class Main {
    public static void main(String[] args)
    {
        if (false)
        {
            System.out.println("Usage: java Simulator <number of readers> <number of writers>");
        }
        else
        {
            final int READERS = 10;
            final int WRITERS = 2;
            Database database = new Database();
            for (int i = 0; i < READERS; i++)
            {
                new Reader(database).start();
            }
            for (int i = 0; i < WRITERS; i++)
            {
                new Writer(database).start();
            }
        }
    }
}
