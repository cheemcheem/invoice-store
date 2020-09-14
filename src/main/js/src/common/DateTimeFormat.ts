const dateTimeFormat = Intl.DateTimeFormat("en-GB", {
  year: 'numeric',
  month: 'numeric',
  day: 'numeric'
});
export default function FormatDate(date: Date) {
  return dateTimeFormat.format(date);
}