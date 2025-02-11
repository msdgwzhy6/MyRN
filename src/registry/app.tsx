import React, { useCallback } from 'react';
import { NavigationContainer, DarkTheme } from '@react-navigation/native';
import { AppProvider, ScreenProps } from '../app.context';
import { CommonScreenProps } from '@navigators/index';
import { isNil } from '@src/utils/utils';
import { StatusBarMode } from '@src/utils/rnBridge';
import { StatusBar } from 'react-native';

export enum ComponentName {
  Home = 'Home',
  Test = 'Test',
}

const App: React.FC<ScreenProps> = props => {
  const { children, ...rest } = props;
  const { routeName = '', ...routeParams } = rest;
  const getScreenOptions = useCallback(
    (routeParams: Omit<ScreenProps, 'routeName'>) => {
      if (
        !isNil(routeParams.statusBarMode) &&
        (routeParams.statusBarMode & StatusBarMode.TRANSPARENT) > 0
      ) {
        CommonScreenProps.headerShown = false;
      }
      if (
        !isNil(routeParams.statusBarMode) &&
        (routeParams.statusBarMode & StatusBarMode.TRANSPARENT) === 0 &&
        ((routeParams.statusBarMode & StatusBarMode.DARK) > 0 ||
          (routeParams.statusBarMode & StatusBarMode.LIGHT) > 0)
      ) {
        CommonScreenProps.headerStatusBarHeight = StatusBar.currentHeight;
      }
      return { ...CommonScreenProps };
    },
    [],
  );
  const content = (
    <NavigationContainer theme={DarkTheme}>
      {React.cloneElement(children as React.ReactElement, {
        routeName,
        routeParams,
        screenOptions: getScreenOptions(routeParams),
      })}
    </NavigationContainer>
  );
  return <AppProvider screenProps={rest}>{content}</AppProvider>;
};

export default App;
