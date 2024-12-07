import tasks from "./elements/tasks";
import events from "./elements/events";
import gateway from "./elements/gateway";
import other from "./elements/other";
import lint from "./lint";
import configForm from "./configForm";
import panel from "./panel";
import toolbar from "./toolbar";

const translations: { [key: string]: any } = {
	elements: {
		...other,
		...events,
		...gateway,
		...tasks
	},
	lint,
	configForm,
	panel,
	toolbar
};

export default translations;
