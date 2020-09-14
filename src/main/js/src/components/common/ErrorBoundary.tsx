import React, {ErrorInfo} from "react";

export default class ErrorBoundary extends React.Component<{ renderError: any }, { hasError: boolean }> {
  constructor(props: { renderError: any }) {
    super(props);
    this.state = ({hasError: false})
  }

  static getDerivedStateFromError(error: Error) {
    console.error("Error in component", error);
    return {hasError: true};
  }

  componentDidCatch(error: Error, errorInfo: ErrorInfo) {
    console.error({error, errorInfo});
  }

  render() {
    if (this.state.hasError) {
      return this.props.renderError;
    }
    return this.props.children;
  }
}