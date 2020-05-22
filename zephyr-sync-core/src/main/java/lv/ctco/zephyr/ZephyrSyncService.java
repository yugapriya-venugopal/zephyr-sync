package lv.ctco.zephyr;

import java.io.IOException;
import java.util.List;

import lv.ctco.zephyr.beans.TestCase;
import lv.ctco.zephyr.service.AuthService;
import lv.ctco.zephyr.service.JiraService;
import lv.ctco.zephyr.service.MetaInfo;
import lv.ctco.zephyr.service.MetaInfoRetrievalService;
import lv.ctco.zephyr.service.TestCaseResolutionService;
import lv.ctco.zephyr.service.ZephyrService;
import lv.ctco.zephyr.util.CustomPropertyNamingStrategy;
import lv.ctco.zephyr.util.ObjectTransformer;

public class ZephyrSyncService {

    private AuthService authService;
    private MetaInfoRetrievalService metaInfoRetrievalService;
    private TestCaseResolutionService testCaseResolutionService;
    private JiraService jiraService;
    private ZephyrService zephyrService;

    public ZephyrSyncService(Config config) {
        ObjectTransformer.setPropertyNamingStrategy(new CustomPropertyNamingStrategy(config));

        authService = new AuthService(config);
        metaInfoRetrievalService = new MetaInfoRetrievalService(config);
        testCaseResolutionService = new TestCaseResolutionService(config);
        jiraService = new JiraService(config);
        zephyrService = new ZephyrService(config);
    }

    public void execute() throws IOException, InterruptedException {
        authService.authenticateInJira();

        MetaInfo metaInfo = metaInfoRetrievalService.retrieve();

        List<TestCase> testCases = testCaseResolutionService.resolveTestCases();

        zephyrService.mapTestCasesToIssues(testCases);

        for (TestCase testCase : testCases) {
            if (testCase.getId() == null) {
                jiraService.createTestIssue(testCase);
                zephyrService.addStepsToTestIssue(testCase);
                jiraService.linkToStory(testCase);
            }
        }

        zephyrService.linkExecutionsToTestCycle(metaInfo, testCases);
        zephyrService.updateExecutionStatuses(testCases);

    }
}
