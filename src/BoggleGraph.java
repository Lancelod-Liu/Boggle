import java.util.HashSet;


public class BoggleGraph {
    private boolean[][] marked;
    private Bag<Point>[][] adj;
    private HashSet<String> words;
    private BoggleBoard board;
    private int M;
    private int N;
    
    private static class Point {
        private int x;
        private int y;
        
        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
        
        public int x() {return x;}
        public int y() {return y;}
        
        public String toString() {
            return String.format("(%2d,%2d)", x, y);
        }
    }
    
    private Iterable<Point> getAdjPoints(Point p) {
        Bag<Point> adjPoint = new Bag<Point>();
        for (int x = p.x() - 1; x < p.x() + 2; x++)
            for (int y = p.y() - 1; y < p.y() + 2; y++) {
                if (x < 0 || x >= M) break;   //skip one outside loop
                if (y < 0 || y >= N) continue; //skip one inside loop
                if (x == p.x() && y == p.y()) continue; // just the point skip one inside loop
                adjPoint.add(new Point(x, y));
            }
        return adjPoint;
    }
    
    @SuppressWarnings("unchecked")
    public BoggleGraph(BoggleBoard board) {
        // TODO Auto-generated constructor stub
        this.board = board;
        M = board.rows();
        N = board.cols();
        marked = new boolean[M][N];
        adj = (Bag<Point>[][]) new Bag[M][N];
        // set up the adj info
        for (int x = 0; x < M; x++)
            for (int y = 0; y < N; y++) {
                adj[x][y] = (Bag<Point>) getAdjPoints(new Point(x, y));
            }
    }

    public Iterable<String> getWordsFrom(int x, int y, DictSET dict) {
        words = new HashSet<String>();
        int depth = 0; // number of points reached
        int length = board.cols() * board.rows();         
        marked = new boolean[M][N]; // clean the marked array
        StringBuilder pathWord = new StringBuilder();
        
        dfs(new Point(x, y), depth, length, pathWord, dict);
        
        return words;
    }    
    
    private void dfs(Point p, int depth, int length, StringBuilder pathWord, DictSET dict) {
        marked[p.x()][p.y()] = true;  
        char c = board.getLetter(p.x(), p.y());
        depth++;
        // Build fullword
        // special case Q -> Qu
        if (c == 'Q')
            pathWord.append("QU");
        else
            pathWord.append(c);      
        String fullword = pathWord.toString();
        // if not a prefix stop search
        if (false == isPrefix(fullword, dict)) return;
        // path is in the dict
        if (dict.contains(fullword)) {
            // depth == 2 and has a QU
            if (depth == 2 && fullword.contains("QU"))
                words.add(fullword);
            else if (depth > 2)
                words.add(fullword);
        }      
        // search done
        if (depth == length) {
            return;
        }        
        for (Point pp : adj[p.x()][p.y()]) {
            if (marked[pp.x()][pp.y()] == false) {                
                dfs(pp, depth, length, pathWord, dict);
                marked[pp.x()][pp.y()] = false; 
                int len = pathWord.length();
                pathWord.deleteCharAt(len - 1);
                if (pathWord.charAt(pathWord.length() - 1) == 'Q')
                    pathWord.deleteCharAt(len - 2);
            }                
        }
    }
    
    private boolean isPrefix(String pathWord, DictSET dict) {
        return dict.hasKeyWithPrefix(pathWord);
    }
    
    public String toString() {  
        String s = new String();

        for (String ss : words)
            s += "[" + ss + "]";
        
        return s;
    }
    

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);

        
        Stopwatch t = new Stopwatch();
        int i = 0;
        while (i < 2000) {
            solver.getAllValidWords(board);
            i++;
        }
        StdOut.print(t.elapsedTime());
    }

}
