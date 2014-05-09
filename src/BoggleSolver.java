import java.util.HashSet;


public class BoggleSolver {
    private DictSET dict;
    private HashSet<String> paths;
    
    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary)
    {
        dict = new DictSET();
        for (String s : dictionary) {
            dict.add(s);
        }
        
    }
    // Finding all possible paths usin given start point
    private Iterable<String> getWordsFrom(int x, int y, BoggleBoard board, BoggleGraph graph)
    {
        return (HashSet<String>) graph.getWordsFrom(x, y, dict);
    }    
    
    private Iterable<String> getAllPaths(BoggleBoard board)
    {
        paths = new HashSet<String>();
        BoggleGraph graph = new BoggleGraph(board);
        // (i, j) represents just the point(i, j) in board
        for (int i = 0; i < board.rows(); i++)
            for (int j = 0; j < board.cols(); j++) {
                // put all the paths from (i,j)
                paths.addAll((HashSet<String>) getWordsFrom(i, j, board, graph));
               // StdOut.printf("%d %d done! paths = %s\n", i, j, paths.toString());
            }
        return paths;
    }
    
    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board)
    { 
        return (HashSet<String>) getAllPaths(board);
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word)
    {
        if (!dict.contains(word))
            return 0;
        switch (word.length()) {
            case 0:
            case 1:
            case 2: return 0;
            case 3:
            case 4: return 1;
            case 5: return 2;
            case 6: return 3;
            case 7: return 5;
            default: return 11;        
        }
        
    }


    public static void main(String[] args) {
        // TODO Auto-generated method stub

        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board))
        {
            StdOut.println(word);
            score += solver.scoreOf(word); 
        }
        StdOut.println("Score = " + score);
    }

}
