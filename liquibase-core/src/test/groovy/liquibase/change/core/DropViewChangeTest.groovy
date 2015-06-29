package liquibase.change.core

import liquibase.action.ActionStatus;
import liquibase.change.StandardChangeTest;
import liquibase.sdk.database.MockDatabase
import liquibase.snapshot.MockSnapshotGeneratorFactory
import liquibase.snapshot.SnapshotGeneratorFactory
import liquibase.structure.core.View

public class DropViewChangeTest  extends StandardChangeTest {

    def getConfirmationMessage() throws Exception {
        when:
        DropViewChange change = new DropViewChange();
        change.setViewName("VIEW_NAME");

        then:
        "View VIEW_NAME dropped" == change.getConfirmationMessage()
    }

    def "checkStatus"() {
        when:
        def database = new MockDatabase()
        def snapshotFactory = new MockSnapshotGeneratorFactory()
        SnapshotGeneratorFactory.instance = snapshotFactory

        def view = new View(null, null, "test_view")

        def change = new DropViewChange()
        change.viewName = view.name

        then: "view is not there yet"
        assert change.checkStatus(database).status == ActionStatus.Status.applied

        when: "view exists"
        snapshotFactory.addObjects(view)
        then:
        assert change.checkStatus(database).status == ActionStatus.Status.notApplied
    }
}
