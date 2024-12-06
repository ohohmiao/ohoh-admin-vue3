import tasks from "./tasks";
import events from "./events";
import gateway from "./gateway";
import lint from "./lint";
import other from "./other";

const translations: { [key: string]: any } = {
	...other,
	...events,
	...gateway,
	...lint,
	...tasks
};

export default translations;
