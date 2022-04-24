

public class Instructor extends User{
    // private Teams Arraylist<>
    // -> We need to create team class which will contain a list of students
    //for each team. 
    //The students can only access a file if it belongs to this team.
    //The team name will follow the same convention as in the filesystem
    //It will be used for the permissions assignment
    private String name;

    public Instructor(String username, String password, String name) {
        super(username, password);
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
}
