import zh_CN from "./zh_CN";
import { createI18n } from "vue-i18n";

const languages = {
	zh_CN
};

export function customTranslate(template: string, replacements?: Record<string, string>) {
	replacements = replacements || {};

	const translations = languages["zh_CN"];

	// Translate
	template = translations.elements[template] || template;

	// Replace
	return template.replace(/{([^}]+)}/g, function (_, key) {
		return replacements![key] || "{" + key + "}";
	});
}

export const defaultLang = "zh_CN";

export const bpmnI18n = createI18n({
	legacy: false,
	globalInjection: true,
	locale: defaultLang,
	messages: {
		zh_CN
	}
});

export default {
	translate: ["value", customTranslate]
};
