import React from "react";

type FormInputProps = { formInputId: string, formInputType: string, formInputLabelText?: string }

export default function FormInput(props: FormInputProps) {

  const {formInputId, formInputLabelText, formInputType} = props;

  return <>
    {formInputLabelText && <label htmlFor={formInputId}>{formInputLabelText}</label>}
    <input id={formInputId} name={formInputId} type={formInputType} required/>
  </>;
}