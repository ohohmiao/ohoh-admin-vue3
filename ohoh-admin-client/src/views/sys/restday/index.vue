<template>
	<div class="card fc">
		<div class="fc-header-toolbar fc-toolbar fc-toolbar-ltr">
			<div class="fc-toolbar-chunk">
				<div class="fc-button-group">
					<el-button-group>
						<el-button :color="toolbarColor" @click="handlePreMonth">上一月</el-button>
						<el-button :color="toolbarColor" @click="handleNextMonth">下一月</el-button>
					</el-button-group>
				</div>
				<el-button
					:color="toolbarColor"
					@click="handleGotoThisMonth"
					:disabled="thizMonthButtonDisable"
					style="margin-left: 0.75em"
					>今天</el-button
				>
			</div>
			<div class="fc-toolbar-chunk">
				<h2 class="fc-toolbar-title">
					<el-date-picker
						v-model="datePicker"
						type="month"
						@change="handleDatePickerChange"
						:editable="false"
						:clearable="false"
					></el-date-picker>
				</h2>
			</div>
			<div class="fc-toolbar-chunk">
				<div class="fc-button-group">
					<el-button-group>
						<el-button :color="toolbarColor" @click="handlePreYear">上一年</el-button>
						<el-button :color="toolbarColor" @click="handleNextYear">下一年</el-button>
					</el-button-group>
				</div>
			</div>
		</div>
		<FullCalendar ref="calendarRef" :options="calendarOptions"></FullCalendar>
	</div>
</template>

<script setup lang="ts">
import { ref, Ref } from "vue";
import FullCalendar from "@fullcalendar/vue3";
import dayGridPlugin from "@fullcalendar/daygrid";
import interactionPlugin from "@fullcalendar/interaction";
import { DateClickArg } from "@fullcalendar/interaction";
import { CalendarOptions } from "@fullcalendar/core";
import { listSysRestDay4FCApi, addSysRestDayApi, deleteSysRestDayApi } from "@/api/modules/sys/restday";
import dayjs from "dayjs";
import solarLunar from "solarLunar-es";

// 节假日标题
const restDayTitle = "休";
// 当前年月
const thizDayjsMonth = dayjs().format("YYYY-MM");
// toolbar按钮颜色
const toolbarColor = "#191a20";

const calendarRef = ref<InstanceType<typeof FullCalendar>>();
const thizMonthButtonDisable = ref(true);
const datePicker = ref();

// 日历面板日期点击，切换是否节假日
const handleDateClick = (info: DateClickArg) => {
	handleUpdateEvent({
		id: info.dateStr,
		title: restDayTitle,
		start: info.date,
		allDay: true,
		color: "#6aba49",
		textColor: "#fff"
	});
};

const handleUpdateEvent = (newEventData: any) => {
	const calendarApi = calendarRef.value?.getApi();
	if (calendarApi) {
		const existingEvent = calendarApi
			.getEvents()
			.find(event => event.startStr === newEventData.id && event.title == restDayTitle);
		if (existingEvent) {
			// 删除
			existingEvent.remove();
			deleteSysRestDayApi({ date: newEventData.id });
		} else {
			// 新增
			calendarApi.addEvent(newEventData);
			addSysRestDayApi({ date: newEventData.id });
		}
	}
};

const calendarOptions: Ref<CalendarOptions> = ref({
	locale: "zh-cn",
	contentHeight: 530,
	datesSet: fetchInfo => {
		const currentDate = dayjs(fetchInfo.view.currentStart).format("YYYY-MM");
		datePicker.value = currentDate;
		thizMonthButtonDisable.value = currentDate == thizDayjsMonth;
	},
	events: async (fetchInfo, successCallback) => {
		// 先清空旧面板event
		const calendarApi = calendarRef.value?.getApi();
		if (calendarApi) {
			calendarApi.getEvents().forEach(event => event.remove());
		}
		// 请求接口，获取当前日期期间的休息日fullcalendar event数据
		const { data } = await listSysRestDay4FCApi({
			start: dayjs(fetchInfo.start).format("YYYY-MM-DD"),
			end: dayjs(fetchInfo.end).format("YYYY-MM-DD")
		});
		// 组装当前日期期间内的节日event
		for (const date = fetchInfo.start; date <= fetchInfo.end; date.setDate(date.getDate() + 1)) {
			const lunarDate = solarLunar.solar2lunar(date.getFullYear(), date.getMonth() + 1, date.getDate());
			if (typeof lunarDate != "number" && lunarDate.festival) {
				data.push({
					title: lunarDate.festival,
					start: dayjs(date).format("YYYY-MM-DD")
				});
			}
			if (typeof lunarDate != "number" && lunarDate.lunarFestival) {
				data.push({
					title: lunarDate.lunarFestival,
					start: dayjs(date).format("YYYY-MM-DD")
				});
			}
		}
		successCallback(data);
	},
	plugins: [dayGridPlugin, interactionPlugin],
	initialView: "dayGridMonth",
	headerToolbar: false,
	dayCellContent: ({ date }) => {
		const lunarDate = solarLunar.solar2lunar(date.getFullYear(), date.getMonth() + 1, date.getDate());
		if (typeof lunarDate != "number") {
			return {
				html: `<div title="${dayjs(date).format("YYYY-MM-DD")}"><div style="float:left">
								${lunarDate.lDay === 1 ? lunarDate.monthCn + lunarDate.dayCn : lunarDate.dayCn}
				  </div>
					<div style="float:right">${date.getDate()}日</div></div>`
			};
		} else {
			return {
				html: `<div title="${dayjs(date).format("YYYY-MM-DD")}">${date.getDate()}日</div>`
			};
		}
	},
	dateClick: handleDateClick
});

// 年月选择器变更
const handleDatePickerChange = () => {
	const calendarApi = calendarRef.value?.getApi();
	calendarApi?.gotoDate(dayjs(datePicker.value).format("YYYY-MM-DD"));
};

// 上一月
const handlePreMonth = () => {
	const calendarApi = calendarRef.value?.getApi();
	calendarApi?.prev();
};

// 下一月
const handleNextMonth = () => {
	const calendarApi = calendarRef.value?.getApi();
	calendarApi?.next();
};

// 跳转这个月
const handleGotoThisMonth = () => {
	const calendarApi = calendarRef.value?.getApi();
	calendarApi?.today();
};

// 上一年
const handlePreYear = () => {
	const calendarApi = calendarRef.value?.getApi();
	calendarApi?.prevYear();
};

// 下一年
const handleNextYear = () => {
	const calendarApi = calendarRef.value?.getApi();
	calendarApi?.nextYear();
};
</script>

<style lang="scss" scoped>
.fc {
	:deep(.fc-header-toolbar) {
		font-size: 14px;
	}

	:deep(table) {
		.fc-header-toolbar {
			font-size: 14px;
		}

		thead > tr > th div {
			font-weight: bold;
			color: #ed6d23;
			font-size: 14px;
		}

		.fc-day-sat .fc-daygrid-day-number,
		.fc-day-sat .fc-col-header-cell-cushion,
		.fc-day-sun .fc-daygrid-day-number,
		.fc-day-sun .fc-col-header-cell-cushion {
			color: #25992e !important;
		}

		.fc-daygrid-day-top .fc-daygrid-day-number {
			width: 100%;
			text-align: right;
			font-weight: 600;
			padding: 12px 12px 0 12px;
		}

		.fc-daygrid-day {
			cursor: pointer;
		}

		.fc-daygrid-event {
			z-index: -1;
		}
	}
}
</style>
