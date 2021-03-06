import React from 'react';
import {render, waitForElement} from '@testing-library/react';
import App from './App';

test('renders app', async () => {
  const {getAllByText} = render(<App/>);

  const text = await waitForElement(() => getAllByText(/Invoice/));

  text.forEach(t => expect(t).toBeInTheDocument());
});
