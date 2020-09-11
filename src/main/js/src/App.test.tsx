import React from 'react';
import {render,waitForElement} from '@testing-library/react';
import App from './App';
import {getByText} from "@testing-library/react";

test('renders app',async () => {
  const {getByText} = render(<App/>);

  const text = await waitForElement(() => getByText(/Invoice Store/));

  expect(text).toBeInTheDocument();
});
