package com.a1qa.model.domain;

import javax.persistence.*;

@Entity
@Table(name = "variant")

public class Variant extends ABaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "is_dynamic_version_in_footer")
    private boolean isDynamicVersionInFooter;

    @Column(name = "use_ajax_for_tests_page")
    private boolean useAjaxForTestsPage;

    @Column(name = "use_frame_for_new_project")
    private boolean useFrameForNewProject;

    @Column(name = "use_new_tab_for_new_project")
    private boolean useNewTabForNewProject;

    @Column(name = "use_geolocation_for_new_project")
    private boolean useGeolocationForNewProject;

    @Column(name = "use_alert_for_new_project")
    private boolean useAlertForNewProject;

    @Column(name = "grammar_mistake_on_save_project")
    private boolean grammarMistakeOnSaveProject;

    @Column(name = "grammar_mistake_on_save_test")
    private boolean grammarMistakeOnSaveTest;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isDynamicVersionInFooter() {
        return isDynamicVersionInFooter;
    }

    public void setDynamicVersionInFooter(boolean dynamicVersionInFooter) {
        isDynamicVersionInFooter = dynamicVersionInFooter;
    }

    public boolean isUseAjaxForTestsPage() {
        return useAjaxForTestsPage;
    }

    public void setUseAjaxForTestsPage(boolean useAjaxForTestsPage) {
        this.useAjaxForTestsPage = useAjaxForTestsPage;
    }

    public boolean isUseFrameForNewProject() {
        return useFrameForNewProject;
    }

    public void setUseFrameForNewProject(boolean useFrameForNewProject) {
        this.useFrameForNewProject = useFrameForNewProject;
    }

    public boolean isUseNewTabForNewProject() {
        return useNewTabForNewProject;
    }

    public void setUseNewTabForNewProject(boolean useNewTabForNewProject) {
        this.useNewTabForNewProject = useNewTabForNewProject;
    }

    public boolean isUseGeolocationForNewProject() {
        return useGeolocationForNewProject;
    }

    public void setUseGeolocationForNewProject(boolean useGeolocationForNewProject) {
        this.useGeolocationForNewProject = useGeolocationForNewProject;
    }

    public boolean isUseAlertForNewProject() {
        return useAlertForNewProject;
    }

    public void setUseAlertForNewProject(boolean useAlertForNewProject) {
        this.useAlertForNewProject = useAlertForNewProject;
    }

    public boolean isGrammarMistakeOnSaveProject() {
        return grammarMistakeOnSaveProject;
    }

    public void setGrammarMistakeOnSaveProject(boolean grammarMistakeOnSaveProject) {
        this.grammarMistakeOnSaveProject = grammarMistakeOnSaveProject;
    }

    public boolean isGrammarMistakeOnSaveTest() {
        return grammarMistakeOnSaveTest;
    }

    public void setGrammarMistakeOnSaveTest(boolean grammarMistakeOnSaveTest) {
        this.grammarMistakeOnSaveTest = grammarMistakeOnSaveTest;
    }


}
