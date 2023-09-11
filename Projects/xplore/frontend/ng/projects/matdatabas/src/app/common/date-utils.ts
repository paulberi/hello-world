import moment from "moment";

const getOffsetDate = () => {
  const tzoffset = (new Date()).getTimezoneOffset() * 60000; // offset in milliseconds
  return new Date(Date.now() - tzoffset);
};

/**
 * Returns the date and time in a format like so: "2020-01-31T14:04"
 */
export const getLocalDateTimeISOString = (date?) => {
  if (date == null) {
    date = getOffsetDate();
  }
  return date.toISOString().slice(0, 16);
};

/**
 * Return the date in a format like so: "2020-01-30"
 */
export const getLocalDateISOString = (date?) => {
  if (date == null) {
    date = getOffsetDate();
  }
  return date.toISOString().slice(0, 10);
};

/**
 * Formats a string like 2020-03-26T08:42:34.302323 into 2020-03-26 08:42:34
 */
export const prettifyISODateString = (date: string) => {
  if (date == null) {
    return null;
  }
  const match = date.match(/^([\d-]+)T([\d:]+).+$/);
  if (match != null) {
    return match[1] + " " + match[2];
  }
  return date;
};

export function momentDateToISOString(date: moment.Moment) {
  if (date == null) {
    return null;
  }

  return date.local().format("YYYY-MM-DD");
}

export function momentDateEndOfDayToISOString(date: moment.Moment) {
  if (date == null) {
    return null;
  }

  const lastMoment = date.local().endOf("day");
  return lastMoment.format("YYYY-MM-DDTHH:mm:ss.SSS");
}

export function momentDateStartOfDayToISOString(date: moment.Moment) {
  if (date == null) {
    return null;
  }

  const lastMoment = date.local().startOf("day");
  return lastMoment.format("YYYY-MM-DDTHH:mm:ss.SSS");
}

export function momentFromDateAndTime(date: moment.Moment, timeString: string) {
  const time = moment(timeString, "HH:mm");

  const newDate = moment(date);
  newDate.hour(time.hour());
  newDate.minutes(time.minutes());

  return newDate;
}
