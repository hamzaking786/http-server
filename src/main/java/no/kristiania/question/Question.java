package no.kristiania.question;

public class Question {

    private String title;
    private String lowLabel;
    private String highLabel;

    private long id;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getHighLabel() {
        return highLabel;
    }

    public void setHighLabel(String highLabel) {
        this.highLabel = highLabel;
    }

    public String getLowLabel() {
        return lowLabel;
    }

    public void setLowLabel(String lowLabel) {
        this.lowLabel = lowLabel;
    }
}
