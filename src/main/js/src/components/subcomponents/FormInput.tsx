import React from "react";
import {FormControl, Input, InputLabel} from "@material-ui/core";

type FormInputProps = { formInputId: string, formInputType: string, formInputLabelText?: string }

export default function FormInput(props: FormInputProps) {

  const {formInputId, formInputLabelText, formInputType} = props;

  return <>
    <FormControl>
      {formInputLabelText && <InputLabel htmlFor={formInputId}>{formInputLabelText}</InputLabel>}
      <Input id={formInputId} name={formInputId} type={formInputType} required/>
    </FormControl>
  </>;
}